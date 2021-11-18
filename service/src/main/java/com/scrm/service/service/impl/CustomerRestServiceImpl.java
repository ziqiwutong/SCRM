package com.scrm.service.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baidubce.http.ApiExplorerClient;
import com.baidubce.http.AppSigner;
import com.baidubce.http.HttpMethodName;
import com.baidubce.model.ApiExplorerRequest;
import com.baidubce.model.ApiExplorerResponse;
import com.scrm.service.service.CustomerRestService;
import com.scrm.service.vo.FirmRelation;
import com.scrm.service.vo.PhoneAttribution;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;

@Service
public class CustomerRestServiceImpl implements CustomerRestService {

    @Override
    public PhoneAttribution queryPhoneAttribution(String phone) {
        String path = "https://hcapi02.api.bdymkt.com/mobile";
        ApiExplorerRequest request = new ApiExplorerRequest(HttpMethodName.GET, path);
        request.setCredentials(
                "5defc1ad0447491a834e77815c1372f4",
                "8bcb9ec79eda49c29ced1a60c5fe7c2d"
        );
        request.addHeaderParameter("Content-Type", "application/json;charset=UTF-8");
        request.addQueryParameter("mobile", phone);

        ApiExplorerClient client = new ApiExplorerClient(new AppSigner());
        try {
            ApiExplorerResponse response = client.sendRequest(request);
            JSONObject body = JSONObject.parseObject(response.getResult());
            String code = body.getString("code");
            JSONObject data = body.getJSONObject("data");
            if (code != null && code.equals("200")) {
                return new PhoneAttribution(
                        data.getInteger("num"),
                        data.getString("types"),
                        data.getString("isp"),
                        data.getString("prov"),
                        data.getString("city"),
                        data.getString("area_code"),
                        data.getString("city_code"),
                        data.getString("zip_code")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String scanBusinessCard(String url) {
        String path = "https://aip.baidubce.com/rest/2.0/ocr/v1/business_card";
        ApiExplorerRequest request = new ApiExplorerRequest(HttpMethodName.POST, path);
        request.addHeaderParameter("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
        String accessToken = "";
        request.setJsonBody("url=" + url);

        ApiExplorerClient client = new ApiExplorerClient();
        try {
            ApiExplorerResponse response = client.sendRequest(request);
            // 返回结果格式为Json字符串
            System.out.println(response.getResult());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<ArrayList<FirmRelation>> queryRelationBetweenFirm(String firmA, String firmB) {
        RestTemplate rest = new RestTemplate();

//        String appKey = "86b6b347-4143-48ad-b8ff-51592d446a40";
//        String secretKey = "1c90b493-48a3-473c-89a3-7f08e1989b28";
//        String enterprises = firmA + "," + firmB;
//        String jobUrl = "https://api.qixin.com/APIService/relation/createFindRelationTask" +
//                "?appkey=" + appKey +
//                "&secret_key=" + secretKey +
//                "&enterprises=" +
//                enterprises;

        String jobUrl = "https://www.fastmock.site/mock/e89826b10151d3ddafd81e87b0cf7110/api/createFindRelationTask";

        String key;
        JSONObject data;
        try {
            HttpEntity<String> jobResponse = rest.exchange(jobUrl, HttpMethod.GET, null, String.class);
            JSONObject body = JSONObject.parseObject(jobResponse.getBody());
            String status = body.getString("status");
            if (!(status != null && status.equals("200"))) {
                return null;
            }
            String message = body.getString("message");
            if (!(message != null && message.equals("操作成功"))) {
                return null;
            }
            data = body.getJSONObject("data");
            key = data.getString("key");
            if (key == null) return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

//        String infoUrl = "https://api.qixin.com/APIService/relation/getFindRelationResult" +
//                "?appkey=" + appKey +
//                "&secret_key=" + secretKey +
//                "&key=" +
//                key;

        String infoUrl = "https://www.fastmock.site/mock/e89826b10151d3ddafd81e87b0cf7110/api/getFindRelationResult" +
                "?key=" +
                key;

        try {
            HttpEntity<String> infoResponse = rest.exchange(infoUrl, HttpMethod.GET, null, String.class);
            JSONObject body = JSONObject.parseObject(infoResponse.getBody());
            String status = body.getString("status");
            if (!(status != null && status.equals("200"))) {
                return null;
            }
            String message = body.getString("message");
            if (!(message != null && message.equals("操作成功"))) {
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
