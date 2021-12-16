package com.scrm.manage.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.scrm.manage.dao.DepartmentDao;
import com.scrm.manage.dao.UserInfoDao;
import com.scrm.manage.entity.Department;
import com.scrm.manage.entity.UserInfo;
import com.scrm.manage.service.IuapService;
import com.scrm.manage.service.UserService;
import com.scrm.manage.vo.User;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private IuapService iuapService;

    @Resource
    private UserInfoDao userInfoDao;

    @Resource
    private DepartmentDao departmentDao;

    @Resource(name = "otherRestTemplate")
    private RestTemplate otherRestTemplate;

    private final RestTemplate restTemplate;

    public UserServiceImpl() {
        restTemplate = new RestTemplate();
        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        mappingJackson2HttpMessageConverter.setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN, MediaType.APPLICATION_OCTET_STREAM));
        restTemplate.getMessageConverters().add(mappingJackson2HttpMessageConverter);
        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
    }
    
    @Override
    public List<User> query(Integer count, String preId, String departmentId) {
        ArrayList<User> result = new ArrayList<>();

        String token = iuapService.getAccessToken();

        String url = "https://openapi.yonyoucloud.com/user/";
        if (departmentId.equals("")) {
            url = url + "get?access_token=" + token + "&count=" + count;
            if (!preId.equals("")) {
                url = url + "&next_id=" + preId;
            }
        } else {
            url = url + "dept/" + departmentId + "/" + count;
            if (!preId.equals("")) {
                url = url + "/" + preId;
            }
            url = url + "?access_token=" + token;
        }
        ResponseEntity<String> respEntity = restTemplate.getForEntity(url, String.class);

        JSONObject body = JSONObject.parseObject(respEntity.getBody());
        if (responseFail(body)) return result;

        JSONArray data = body.getJSONArray("data");
        for (int i = 0; i < data.size(); i++) {
            JSONObject info = data.getJSONObject(i);
            result.add(generateUser(info, false));
        }

        return result;
    }

    @Override
    public User queryById(Long id) {
        String token = iuapService.getAccessToken();

        String url = "https://openapi.yonyoucloud.com/user/get?access_token=" + token +
                "&next_id=" + (id - 1) + "&count=1";
        ResponseEntity<String> respEntity = restTemplate.getForEntity(url, String.class);

        JSONObject body = JSONObject.parseObject(respEntity.getBody());
        if (responseFail(body)) return null;

        JSONArray data = body.getJSONArray("data");
        if (data == null || data.size() < 1) return null;
        JSONObject info = data.getJSONObject(0);
        if (info == null || !id.equals(info.getLong("id"))) return null;

        return generateUser(info, true);
    }

    @Override
    public String save(UserInfo userInfo) {
        String url = "http://service/weimob/queryUserInfo?id=" + userInfo.getWeimobId();
        String result = otherRestTemplate.getForObject(url, String.class);
        JSONObject jsonObject = JSON.parseObject(result);

        if (userInfoDao.selectById(userInfo.getId()) == null) {
            if (userInfoDao.insert(userInfo) < 1) return null;
        } else {
            if (userInfoDao.updateById(userInfo) < 1) return null;
        }

        if (jsonObject == null) return "微盟用户信息不存在";
        Integer code = jsonObject.getInteger("code");
        if (code == null || code != 200) return "微盟用户信息不存在";
        JSONObject data = jsonObject.getJSONObject("data");
        return "微盟用户：" + data.getString("nickName");
    }

    @Override
    @Transactional
    public String syncDepartment() {
        QueryWrapper<Department> queryWrapper = new QueryWrapper<>();
        queryWrapper.last(" where 1 = 1");
        departmentDao.delete(queryWrapper);

        int count = departmentDao.selectCount(queryWrapper);
        if (count != 0) return "数据库错误";

        String token = iuapService.getAccessToken();

        String url = "https://openapi.yonyoucloud.com/dept/get?access_token=" + token;

        LinkedList<Department> departments = new LinkedList<>();

        do {
            if (!departments.isEmpty()) {
                Department department = departments.poll();
                url = url + "&parent_id=" + department.getId();
            }
            ResponseEntity<String> respEntity = restTemplate.getForEntity(url, String.class);

            JSONObject body = JSONObject.parseObject(respEntity.getBody());
            if (responseFail(body)) return "用友接口错误";

            JSONArray data = body.getJSONArray("data");
            for (int i = 0; i < data.size(); i++) {
                JSONObject info = data.getJSONObject(i);
                Department department = generateDepartment(info);

                departmentDao.insert(department);
                if (department.getHaveSub() == 1) {
                    departments.offer(department);
                }
            }
        } while (!departments.isEmpty());

        return null;
    }

    @Override
    @Transactional
    public List<Department> queryDepartment() {
        QueryWrapper<Department> queryWrapper = new QueryWrapper<>();

        LinkedList<Department> departments = new LinkedList<>();

        List<Department> result = new ArrayList<>();
        do {
            queryWrapper.clear();

            Department parent = null;
            if (!departments.isEmpty()) {
                parent = departments.poll();
                queryWrapper.eq("parent_id", parent.getId());
            } else {
                queryWrapper.eq("parent_id", 0);
            }

            List<Department> list = departmentDao.selectList(queryWrapper);
            if (list.size() == 0) continue;

            if (parent == null) {
                result = list;
            } else {
                parent.setChildren(new ArrayList<>(list));
            }
            for (Department department : list) {
                if (department.getHaveSub() == 1) {
                    departments.offer(department);
                }
            }
        } while (!departments.isEmpty());

        return result;
    }

    @Override
    public List<Department> queryDepartmentLazy(String id) {
        String token = iuapService.getAccessToken();

        ArrayList<Department> departments = new ArrayList<>();

        String url = "https://openapi.yonyoucloud.com/dept/get?access_token=" + token;
        if (!id.equals("")) {
            url = url + "&parent_id=" + id;
        }
        ResponseEntity<String> respEntity = restTemplate.getForEntity(url, String.class);

        JSONObject body = JSONObject.parseObject(respEntity.getBody());
        if (responseFail(body)) return null;

        JSONArray data = body.getJSONArray("data");
        for (int i = 0; i < data.size(); i++) {
            JSONObject info = data.getJSONObject(i);
            departments.add(generateDepartment(info));
        }

        return departments;
    }

    /**
     * 检查API调用是否失败
     */
    private boolean responseFail(JSONObject body) {
        String status = body.getString("code");
        if (!(status != null && status.equals("0"))) {
            return true;
        }
        String message = body.getString("msg");
        return message == null || !message.equals("成功");
    }

    /**
     * 将用友返回信息转为用户信息
     */
    private User generateUser(JSONObject info, boolean detail) {
        User user = new User();
        user.setId(info.getString("id"));
        user.setName(info.getString("name"));
        user.setDepartmentId(info.getString("dept_id"));
        user.setDuty(info.getString("duty"));
        user.setDutyId(info.getString("duty_id"));
        user.setWorkPhoto(info.getString("work_photo"));
        user.setOfficeDesk(info.getString("office_desk"));
        user.setStaffNo(info.getString("staff_no"));
        user.setUpdateTime(info.getString("updatetime"));
        user.setIsAdmin(info.getInteger("isAdmin"));
        user.setMobile(info.getString("mobile"));
        user.setEmail(info.getString("email"));
        user.setAvatar(info.getString("avatar"));
        user.setSex(info.getString("sex"));
        user.setBirthday(info.getString("birthday"));

        if (detail) {
            UserInfo userInfo = userInfoDao.selectById(info.getString("id"));
            if (userInfo != null) {
                user.setWeimobId(userInfo.getWeimobId());
            }
        }

        return user;
    }

    /**
     * 将用友返回信息转为部门信息
     */
    private Department generateDepartment(JSONObject info) {
        Department department = new Department();
        department.setId(Integer.valueOf(info.getString("id")));
        department.setParentId(info.getInteger("parent_id"));
        department.setLeaderMemberId(info.getInteger("leader_member_id"));
        department.setDepartmentName(info.getString("name"));
        department.setSubName(info.getString("sub_name"));
        department.setType(info.getString("type"));
        department.setMemberCount(info.getInteger("count"));
        department.setHaveSub(info.getInteger("havesub"));
        department.setMessage(info.getString("message"));

        return department;
    }
}
