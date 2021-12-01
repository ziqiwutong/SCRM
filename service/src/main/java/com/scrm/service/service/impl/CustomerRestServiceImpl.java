package com.scrm.service.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baidubce.http.ApiExplorerClient;
import com.baidubce.http.AppSigner;
import com.baidubce.http.HttpMethodName;
import com.baidubce.model.ApiExplorerRequest;
import com.baidubce.model.ApiExplorerResponse;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.scrm.service.dao.CompanyQXBDao;
import com.scrm.service.dao.PhoneAttributionDao;
import com.scrm.service.entity.CompanyQXB;
import com.scrm.service.entity.Customer;
import com.scrm.service.entity.PhoneAttribution;
import com.scrm.service.service.CustomerRestService;
import com.scrm.service.service.CustomerService;
import com.scrm.service.util.FileUtil;
import com.scrm.service.util.ImageCoding;
import com.scrm.service.vo.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class CustomerRestServiceImpl implements CustomerRestService {

    @Resource
    private CustomerService customerService;

    @Resource
    private PhoneAttributionDao phoneAttributionDao;

    @Resource
    private CompanyQXBDao companyQXBDao;

    @Value("${my.file.pic.picRootPath}")
    private String picRootPath;
    @Value("${my.file.pic.picAccessPath}")
    private String picAccessPath;

    // 客户类型
    public static final int PERSONAL = 0;
    public static final int COMPANY = 1;

    // 启信宝key
    private static final String QXB_APP_KEY = "86b6b347-4143-48ad-b8ff-51592d446a40";
    private static final String QXB_SECRET_KEY = "1c90b493-48a3-473c-89a3-7f08e1989b28";

    @Override
    public PhoneAttribution queryPhoneAttribution(String phone) {
        QueryWrapper<PhoneAttribution> wrapper = new QueryWrapper<>();
        wrapper.eq("phone", phone);
        List<PhoneAttribution> phoneAttributionList = phoneAttributionDao.selectList(wrapper);
        if (phoneAttributionList.size() == 1) {
            return phoneAttributionList.get(0);
        }
        if (phoneAttributionList.size() > 1) {
            phoneAttributionDao.delete(wrapper);
        }

        ApiExplorerClient client = new ApiExplorerClient(new AppSigner());

        String path = "https://hcapi02.api.bdymkt.com/mobile";
        ApiExplorerRequest request = new ApiExplorerRequest(HttpMethodName.GET, path);
        request.setCredentials(
                "5defc1ad0447491a834e77815c1372f4",
                "8bcb9ec79eda49c29ced1a60c5fe7c2d"
        );
        request.addHeaderParameter("Content-Type", "application/json;charset=UTF-8");
        request.addQueryParameter("mobile", phone);
        try {
            ApiExplorerResponse response = client.sendRequest(request);
            JSONObject body = JSONObject.parseObject(response.getResult());
            String code = body.getString("code");
            JSONObject data = body.getJSONObject("data");
            if (code != null && code.equals("200")) {
                PhoneAttribution phoneAttribution = new PhoneAttribution();
                phoneAttribution.setPhone(phone);
                phoneAttribution.setSegment(data.getInteger("num"));
                phoneAttribution.setType(data.getString("types"));
                phoneAttribution.setOperator(data.getString("isp"));
                phoneAttribution.setProvince(data.getString("prov"));
                phoneAttribution.setCity(data.getString("city"));
                phoneAttribution.setAreaCode(data.getString("area_code"));
                phoneAttribution.setCityCode(data.getString("city_code"));
                phoneAttribution.setZipCode(data.getString("zip_code"));

                phoneAttributionDao.insert(phoneAttribution);

                return phoneAttribution;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public BusinessCard scanBusinessCard(String url) {
        String token;

        ApiExplorerClient client = new ApiExplorerClient();

        String tokenPath = "https://aip.baidubce.com/oauth/2.0/token";
        ApiExplorerRequest tokenRequest = new ApiExplorerRequest(HttpMethodName.GET, tokenPath);
        tokenRequest.addHeaderParameter("Content-Type", "application/json;charset=UTF-8");
        tokenRequest.addQueryParameter("grant_type", "client_credentials");
        tokenRequest.addQueryParameter("client_id", "1wV3gUmUGLAUk22tYj4igSpB");
        tokenRequest.addQueryParameter("client_secret", "KdoLsWHLuwKdfGuathQY6rakWgyXm8jW");
        try {
            ApiExplorerResponse response = client.sendRequest(tokenRequest);
            JSONObject body = JSONObject.parseObject(response.getResult());
            token = body.getString("access_token");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        String path = "https://aip.baidubce.com/rest/2.0/ocr/v1/business_card";
        try {
            url = picRootPath + url.substring(picAccessPath.length() + 1);
            byte[] imgBytes = FileUtil.readFileByBytes(url, true);
            String imgBase64 = ImageCoding.encode(imgBytes);

            HttpHeaders headers = new HttpHeaders();
            MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
            paramMap.add("image", imgBase64);
            HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(paramMap, headers);
            RestTemplate restTemplate = new RestTemplate();
            List<HttpMessageConverter<?>> list = restTemplate.getMessageConverters();
            for (HttpMessageConverter<?> httpMessageConverter : list) {
                if (httpMessageConverter instanceof StringHttpMessageConverter) {
                    ((StringHttpMessageConverter) httpMessageConverter).setDefaultCharset(StandardCharsets.UTF_8);
                    break;
                }
            }
            ResponseEntity<String> respEntity = restTemplate.postForEntity(
                    path + "?access_token=" + token,
                    httpEntity,
                    String.class
            );
            JSONObject body = JSONObject.parseObject(respEntity.getBody());
            JSONObject data = body.getJSONObject("words_result");
            if (data != null) {
                return new BusinessCard(
                        data.getJSONArray("PC").toArray(),
                        data.getJSONArray("TEL").toArray(),
                        data.getJSONArray("TITLE").toArray(),
                        data.getJSONArray("EMAIL").toArray(),
                        data.getJSONArray("FAX").toArray(),
                        data.getJSONArray("URL").toArray(),
                        data.getJSONArray("ADDR").toArray(),
                        data.getJSONArray("COMPANY").toArray(),
                        data.getJSONArray("MOBILE").toArray(),
                        data.getJSONArray("NAME").toArray()
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    @Override
    public Object[] queryPersonal(String keyword) {
        return queryCustomer(keyword, PERSONAL);
    }

    @Override
    public Object[] queryCompany(String keyword) {
        return queryCustomer(keyword, COMPANY);
    }

    @Override
    public CompanyQXB queryCompanyDetail(String registerNo) {
        QueryWrapper<CompanyQXB> wrapper = new QueryWrapper<>();
        wrapper.eq("register_no", registerNo);
        List<CompanyQXB> companyQXBList = companyQXBDao.selectList(wrapper);
        if (companyQXBList.size() == 1) {
            return companyQXBList.get(0);
        }
        if (companyQXBList.size() > 1) {
            companyQXBDao.delete(wrapper);
        }

        ApiExplorerClient client = new ApiExplorerClient(new AppSigner());

        String path = "https://api.qixin.com/APIService/enterprise/getBasicInfo";
        ApiExplorerRequest request = new ApiExplorerRequest(HttpMethodName.GET, path);
        request.addHeaderParameter("Content-Type", "application/json;charset=UTF-8");
        request.addQueryParameter("appkey", QXB_APP_KEY);
        request.addQueryParameter("secret_key", QXB_SECRET_KEY);
        request.addQueryParameter("keyword", registerNo);
        try {
            ApiExplorerResponse response = client.sendRequest(request);
            JSONObject body = JSONObject.parseObject(response.getResult());
            if (responseFailQXB(body)) {
                return null;
            }
            JSONObject data = body.getJSONObject("data");

            CompanyQXB companyQXB = new CompanyQXB();
            companyQXB.setRegisterNo(data.getString("regNo"));
            companyQXB.setEid(data.getString("id"));
            companyQXB.setCompanyName(data.getString("name"));
            JSONArray historyNames = data.getJSONArray("historyNames");
            if (historyNames != null) {
                StringBuilder sb = new StringBuilder();
                if (historyNames.size() > 0) sb.append(historyNames.getString(0));
                for (int i = 1; i < historyNames.size(); i++) {
                    sb.append(",");
                    sb.append(historyNames.getString(i));
                }
                companyQXB.setHistoryNames(sb.toString());
            } else {
                companyQXB.setHistoryNames("");
            }
            companyQXB.setLegalPerson(data.getString("operName"));
            companyQXB.setBelongOrg(data.getString("belongOrg"));
            companyQXB.setOrgNo(data.getString("orgNo"));
            companyQXB.setCreditNo(data.getString("creditNo"));
            companyQXB.setDistrictCode(data.getString("districtCode"));
            companyQXB.setAddress(data.getString("address"));
            companyQXB.setCompanyKind(data.getString("econKind"));
            switch (data.getString("type_new")) {
                case "01":
                    companyQXB.setTypeNew("大陆企业");
                    break;
                case "02":
                    companyQXB.setTypeNew("社会组织");
                    break;
                case "03":
                    companyQXB.setTypeNew("机关及事业单位");
                    break;
                case "04":
                    companyQXB.setTypeNew("港澳台及国外企业");
                    break;
                case "05":
                    companyQXB.setTypeNew("律所及其他组织机构");
                    break;
                default:
                    companyQXB.setTypeNew("");
                    break;
            }
            switch (data.getString("categoryNew")) {
                case "0115601":
                    companyQXB.setCategoryNew("企业");
                    break;
                case "0115602":
                    companyQXB.setCategoryNew("个体");
                    break;
                case "0115603":
                    companyQXB.setCategoryNew("农民专业合作社");
                    break;
                case "0115699":
                    companyQXB.setCategoryNew("其他类型");
                    break;
                case "0215601":
                    companyQXB.setCategoryNew("社会团体");
                    break;
                case "0215602":
                    companyQXB.setCategoryNew("基金会");
                    break;
                case "0215603":
                    companyQXB.setCategoryNew("民办非企业单位");
                    break;
                case "0215604":
                    companyQXB.setCategoryNew("村民/居民委员会");
                    break;
                case "0215605":
                    companyQXB.setCategoryNew("宗教，工会等其他社会组织");
                    break;
                case "0315601":
                    companyQXB.setCategoryNew("事业单位");
                    break;
                case "0315602":
                    companyQXB.setCategoryNew("机关");
                    break;
                case "0315603":
                    companyQXB.setCategoryNew("其他机构编制");
                    break;
                case "0434401":
                    companyQXB.setCategoryNew("香港企业");
                    break;
                case "0444602":
                    companyQXB.setCategoryNew("澳门企业");
                    break;
                case "0415803":
                    companyQXB.setCategoryNew("台湾企业");
                    break;
                case "0499904":
                    companyQXB.setCategoryNew("国外企业");
                    break;
                case "0515601":
                    companyQXB.setCategoryNew("律所");
                    break;
                case "0500099":
                    companyQXB.setCategoryNew("其他组织机构");
                    break;
                default:
                    companyQXB.setCategoryNew("");
                    break;
            }
            companyQXB.setDomain(data.getString("domain"));
            companyQXB.setScope(data.getString("scope"));
            companyQXB.setRegisterCapital(data.getString("registCapi"));
            companyQXB.setCurrencyUnit(data.getString("currency_unit"));
            companyQXB.setActualCapital(data.getString("actualCapi"));
            companyQXB.setStartDate(data.getString("startDate"));
            companyQXB.setEndDate(data.getString("endDate"));
            companyQXB.setCheckDate(data.getString("checkDate"));
            companyQXB.setRevokeDate(data.getString("revoke_date"));
            companyQXB.setRevokeReason(data.getString("revoke_reason"));
            companyQXB.setEngageStatus(data.getString("status"));
            companyQXB.setStatusNew(data.getString("new_status"));
            companyQXB.setTermStart(data.getString("termStart"));
            companyQXB.setTermEnd(data.getString("termEnd"));
            JSONArray tags = data.getJSONArray("tags");
            if (tags != null) {
                StringBuilder sb = new StringBuilder();
                tag:
                for (int i = 0; i < tags.size(); i++) {
                    switch (tags.getString(i)) {
                        case "1":
                            sb.append("新三板");
                            break;
                        case "6":
                            sb.append("主板上市公司");
                            break;
                        case "40":
                            sb.append("暂停上市");
                            break;
                        case "41":
                            sb.append("终止上市");
                            break;
                        case "9":
                            sb.append("香港上市");
                            break;
                        case "17":
                            sb.append("高新企业");
                            break;
                        default:
                            continue tag;
                    }
                    if (i != 0) sb.append(",");
                }
                companyQXB.setTags(sb.toString());
            } else {
                companyQXB.setTags("");
            }
            companyQXBDao.insert(companyQXB);
            return companyQXB;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<ArrayList<FirmRelation>> queryRelationBetweenFirm(String firmA, String firmB) {
        String key;
        JSONObject data;
        ApiExplorerClient client = new ApiExplorerClient(new AppSigner());

        String path = "https://api.qixin.com/APIService/relation/createFindRelationTask";
        ApiExplorerRequest request = new ApiExplorerRequest(HttpMethodName.GET, path);
        request.addHeaderParameter("Content-Type", "application/json;charset=UTF-8");
        request.addQueryParameter("appkey", QXB_APP_KEY);
        request.addQueryParameter("secret_key", QXB_SECRET_KEY);
        request.addQueryParameter("enterprises", firmA + "," + firmB);
        try {
            ApiExplorerResponse response = client.sendRequest(request);
            JSONObject body = JSONObject.parseObject(response.getResult());
            if (responseFailQXB(body)) {
                return null;
            }
            data = body.getJSONObject("data");
            key = data.getString("key");
            if (key == null) return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        path = "https://api.qixin.com/APIService/relation/getFindRelationResult";
        request = new ApiExplorerRequest(HttpMethodName.GET, path);
        request.addHeaderParameter("Content-Type", "application/json;charset=UTF-8");
        request.addQueryParameter("appkey", QXB_APP_KEY);
        request.addQueryParameter("secret_key", QXB_SECRET_KEY);
        request.addQueryParameter("key", key);
        try {
            ApiExplorerResponse response = client.sendRequest(request);
            JSONObject body = JSONObject.parseObject(response.getResult());
            if (responseFailQXB(body)) {
                return null;
            }
            data = body.getJSONObject("data");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return getRelation(data);
    }

    /**
     * 查个人，查企业
     */
    private Object[] queryCustomer(String keyword, int customerType) {
        Object[] customer = new Object[2];

        // 查客户表
        QueryWrapper<Customer> wrapper = new QueryWrapper<>();
        wrapper.eq("customer_type", customerType);
        wrapper.like("customer_name", keyword);
        customer[0] = customerService.query(wrapper);

        String path;
        switch (customerType) {
            // 查Microsoft必应
            case PERSONAL:
                ArrayList<Object> bing = new ArrayList<>();

                path = "https://api.bing.microsoft.com/v7.0/custom/search";
                String customConfigId = "15d23d8c-99da-46f2-acce-321d8d9e4845";
                String subscriptionKey = "3f1e5b0907af46469ea7777af1fef604";
                try {
                    URL url = new URL(path +
                            "?q=" + URLEncoder.encode(keyword, "UTF-8") +
                            "&CustomConfig=" + customConfigId
                    );
                    HostnameVerifier hv = (urlHostName, session) -> {
                        System.out.println("Warning: URL Host: " + urlHostName + " vs. " + session.getPeerHost());
                        return true;
                    };
                    HttpsURLConnection.setDefaultHostnameVerifier(hv);
                    HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
                    connection.setRequestProperty("Ocp-Apim-Subscription-Key", subscriptionKey);

                    InputStream stream = connection.getInputStream();
                    String response = new Scanner(stream).useDelimiter("\\A").next();
                    stream.close();

                    JSONObject body = JSONObject.parseObject(response);

                    JSONObject data = body.getJSONObject("webPages");
                    if (data != null) {
                        JSONArray webPages = data.getJSONArray("value");
                        if (webPages != null) {
                            for (int i = 0; i < webPages.size(); i++) {
                                JSONObject webPage = webPages.getJSONObject(i);
                                bing.add(new BingSearch(
                                        webPage.getString("name"),
                                        webPage.getString("url"),
                                        webPage.getString("snippet")
                                ));
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    customer[1] = bing;
                }

                break;

            // 查启信宝
            case COMPANY:
                ArrayList<Object> qxb = new ArrayList<>();

                ApiExplorerClient client = new ApiExplorerClient(new AppSigner());

                path = "https://api.qixin.com/APIService/v2/search/advSearch";
                ApiExplorerRequest request = new ApiExplorerRequest(HttpMethodName.GET, path);
                request.addHeaderParameter("Content-Type", "application/json;charset=UTF-8");
                request.addQueryParameter("appkey", QXB_APP_KEY);
                request.addQueryParameter("secret_key", QXB_SECRET_KEY);
                request.addQueryParameter("keyword", keyword);
                request.addQueryParameter("matchType", "ename");
                try {
                    ApiExplorerResponse response = client.sendRequest(request);
                    JSONObject body = JSONObject.parseObject(response.getResult());
                    if (!responseFailQXB(body)) {
                        JSONObject data = body.getJSONObject("data");
                        JSONArray items = data.getJSONArray("items");
                        for (int i = 0; i < items.size(); i++) {
                            JSONObject item = items.getJSONObject(i);
                            qxb.add(new Firm(
                                    item.getString("id"),
                                    item.getString("name"),
                                    item.getString("reg_no"),
                                    item.getString("start_date"),
                                    item.getString("credit_no"),
                                    item.getString("oper_name"),
                                    item.getString("matchType"),
                                    item.getString("matchItems"),
                                    item.getInteger("type")
                            ));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    customer[1] = qxb;
                }

            default:
                break;
        }

        return customer;
    }

    /**
     * 检查启信宝API调用是否失败
     */
    private boolean responseFailQXB(JSONObject body) {
        String status = body.getString("status");
        if (!(status != null && status.equals("200"))) {
            return true;
        }
        String message = body.getString("message");
        return message == null || !message.equals("操作成功");
    }

    /**
     * 根据查询的关系图，生成线性关系
     */
    private ArrayList<ArrayList<FirmRelation>> getRelation(JSONObject data) {
        ArrayList<Node> key = new ArrayList<>();

        // 读取节点
        JSONArray nodeJson = data.getJSONArray("nodes");
        HashMap<String, Node> nodes = new HashMap<>();
        for (Object o : nodeJson) {
            JSONObject item = JSONObject.parseObject(o.toString());
            if (item != null) {
                Node node = new Node();
                node.setId(item.getString("uid"));
                node.setName(item.getString("name"));
                node.setKey(item.getBoolean("iskey"));

                if (node.isKey()) {
                    key.add(node);
                }

                nodes.put(node.getId(), node);
            }
        }

        // 读取关系
        JSONArray linkJson = data.getJSONArray("links");
        for (Object o : linkJson) {
            JSONObject item = JSONObject.parseObject(o.toString());
            if (item != null) {
                String source = item.getString("source_id");
                if (!nodes.containsKey(source)) continue;
                String target = item.getString("target_id");
                if (!nodes.containsKey(target)) continue;
                String label = item.getString("label");

                Link to = new Link(target, 1, label);
                Link back = new Link(source, 0, label);

                nodes.get(source).addLink(to);
                nodes.get(target).addLink(back);
            }
        }

        if (key.size() < 2) return null;

        return searchPath(nodes, key.get(0).getId(), key.get(1).getId());
    }

    /**
     * 深度优先搜索图中两点间的所有路径
     */
    private ArrayList<ArrayList<FirmRelation>> searchPath(HashMap<String, Node> nodes, String start, String end) {
        ArrayList<ArrayList<FirmRelation>> result = new ArrayList<>();
        LinkedList<Link> stack = new LinkedList<>();

        Node current = nodes.get(start);
        current.setFlag(true);
        while (true) {
            // 没有未搜索的相邻节点
            if (current.deadEnd()) {
                current.setLinkIndex(0);
                // 起点判断，跳出循环
                if (stack.isEmpty()) {
                    break;
                }
                stack.pop();
                String nodeId = stack.isEmpty() ? start : stack.peek().getTarget();
                current.setFlag(false);
                current = nodes.get(nodeId);
                continue;
            }
            Link link = current.getLink();
            current.nextLink();
            // 终点
            if (link.getTarget().equals(end)) {
                // 输出结果
                stack.push(link);
                ArrayList<FirmRelation> firmRelation = new ArrayList<>();
                firmRelation.add(new FirmRelation(0, nodes.get(start).getName(), null));
                for (int i = stack.size() - 1; i >= 0; i--) {
                    Link item = stack.get(i);
                    firmRelation.add(new FirmRelation(
                            item.getDirection(),
                            nodes.get(item.getTarget()).getName(),
                            item.getLabel()
                    ));
                }
                result.add(firmRelation);
                if (result.size() >= 5000) break;
                stack.pop();
                continue;
            }
            Node next = nodes.get(link.getTarget());
            // 已经访问过
            if (next.isFlag()) {
                continue;
            }
            next.setFlag(true);
            stack.push(link);
            current = next;
        }

        result.sort(Comparator.comparingInt(ArrayList::size));

        return new ArrayList<>(result.subList(0, Math.min(result.size(), 10)));
    }

    @Data
    private static class Node {

        private String id;

        private String name;

        private boolean key;

        private ArrayList<Link> links;

        private boolean flag;

        private int linkIndex;

        Node() {
            links = new ArrayList<>();
        }

        void addLink(Link link) {
            links.add(link);
        }

        boolean deadEnd() {
            return linkIndex == links.size();
        }

        Link getLink() {
            return links.get(linkIndex);
        }

        void nextLink() {
            linkIndex++;
        }
    }

    @Data
    @AllArgsConstructor
    private static class Link {

        private String target;

        private int direction;

        private String label;
    }
}
