package com.scrm.service.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.scrm.service.service.CustomerRestService;
import com.scrm.service.vo.FirmRelation;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

@Service
public class CustomerRestServiceImpl implements CustomerRestService {

    @Override
    public ArrayList<ArrayList<FirmRelation>> queryRelationBetweenFirm(String firmA, String firmB) {
        RestTemplate rest = new RestTemplate();

//        String enterprises = firmA + "," + firmB;
//        String jobUrl = "https://api.qixin.com/APIService/relation/createFindRelationTask" +
//                "?appkey=86b6b347-4143-48ad-b8ff-51592d446a40" +
//                "&secret_key=1c90b493-48a3-473c-89a3-7f08e1989b28" +
//                "&enterprises=" +
//                enterprises;

        String jobUrl = "https://www.fastmock.site/mock/e89826b10151d3ddafd81e87b0cf7110/api/createFindRelationTask";

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
        JSONObject data = body.getJSONObject("data");
        String key = data.getString("key");
        if (key == null) return null;

//        String infoUrl = "https://api.qixin.com/APIService/relation/getFindRelationResult" +
//                "?appkey=86b6b347-4143-48ad-b8ff-51592d446a40" +
//                "&secret_key=1c90b493-48a3-473c-89a3-7f08e1989b28" +
//                "&key=" +
//                key;

        String infoUrl = "https://www.fastmock.site/mock/e89826b10151d3ddafd81e87b0cf7110/api/getFindRelationResult" +
                "?key=" +
                key;

        HttpEntity<String> infoResponse = rest.exchange(infoUrl, HttpMethod.GET, null, String.class);

        body = JSONObject.parseObject(infoResponse.getBody());
        status = body.getString("status");
        if (!(status != null && status.equals("200"))) {
            return null;
        }
        message = body.getString("message");
        if (!(message != null && message.equals("操作成功"))) {
            return null;
        }
        data = body.getJSONObject("data");

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
        return result;
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
