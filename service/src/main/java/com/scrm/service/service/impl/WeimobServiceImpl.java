package com.scrm.service.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baidubce.http.ApiExplorerClient;
import com.baidubce.http.AppSigner;
import com.baidubce.http.HttpMethodName;
import com.baidubce.model.ApiExplorerRequest;
import com.baidubce.model.ApiExplorerResponse;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.scrm.service.dao.OrderDao;
import com.scrm.service.dao.OrderProductDao;
import com.scrm.service.dao.ProductDao;
import com.scrm.service.entity.Order;
import com.scrm.service.entity.OrderProduct;
import com.scrm.service.entity.Product;
import com.scrm.service.service.WeimobService;
import com.scrm.service.util.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class WeimobServiceImpl implements WeimobService {

    @Resource
    private ProductDao productDao;

    @Resource
    private OrderDao orderDao;

    @Resource
    private OrderProductDao orderProductDao;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Value("${docking.weimob.client.id}")
    private String clientId;

    @Value("${docking.weimob.client.secret}")
    private String clientSecret;

    @Override
    public List<Product> queryProduct(Integer pageCount, Integer currentPage) {
        return productDao.queryWeimobProduct((currentPage - 1) * pageCount, pageCount);
    }

    @Override
    public Integer queryProductCount() {
        return productDao.queryWeimobCount();
    }

    @Override
    @Transactional
    public String syncProduct() {
        String token = getToken();
        if (token == null) {
            return "Access Token 获取失败";
        }
        ApiExplorerClient client = new ApiExplorerClient(new AppSigner());
        String path = "https://dopen.weimob.com/api/1_0/ec/goods/queryGoodsList";
        ApiExplorerRequest request = new ApiExplorerRequest(HttpMethodName.POST, path);
        request.addHeaderParameter("Content-Type", "application/json;charset=UTF-8");
        request.addQueryParameter("accesstoken", token);
        int pageNum = 1;
        while (true) {
            JSONObject json = new JSONObject();
            json.put("pageSize", 20);
            json.put("pageNum", pageNum);
            request.setJsonBody(json.toString());
            try {
                ApiExplorerResponse response = client.sendRequest(request);
                JSONObject body = JSONObject.parseObject(response.getResult());
                if (responseFail(body)) return "产品同步失败";
                JSONObject data = body.getJSONObject("data");
                JSONArray productList = data.getJSONArray("pageList");
                for (int i = 0; i < productList.size(); i++) {
                    JSONObject item = productList.getJSONObject(i);
                    String id = "02_" + item.getLong("goodsId");
                    List<Product> list = productDao.queryBySourceId(id);
                    Product product = list.size() > 0 ? list.get(0) : new Product();
                    product.setSourceId(id);
                    product.setProductName(item.getString("title"));
                    product.setProductImage(item.getString("defaultImageUrl"));
                    product.setProductSales(item.getInteger("salesNum"));
                    product.setProductInventory(item.getInteger("avaliableStockNum"));
                    product.setProductPrice(item.getBigDecimal("minPrice"));
                    product.setRetailPrice(item.getBigDecimal("minPrice"));
                    product.setWholesalePrice(item.getBigDecimal("minPrice"));
                    String priceDescribe = item.getBigDecimal("minPrice") +
                            " ~ " + item.getBigDecimal("maxPrice");
                    product.setPriceDescribe(priceDescribe);
                    if (list.size() > 0) {
                        productDao.updateById(product);
                    } else {
                        productDao.insert(product);
                    }
                }
                int totalCount = data.getInteger("totalCount");
                if (pageNum * 20 >= totalCount) break;
                pageNum++;
            } catch (Exception e) {
                e.printStackTrace();
                return "产品同步失败";
            }
        }
        return null;
    }

    @Override
    @Transactional
    public String syncOrder() {
        String token = getToken();
        if (token == null) {
            return "Access Token 获取失败";
        }
        ApiExplorerClient client = new ApiExplorerClient(new AppSigner());
        String path = "https://dopen.weimob.com/api/1_0/ec/order/queryOrderList";
        ApiExplorerRequest request = new ApiExplorerRequest(HttpMethodName.POST, path);
        request.addHeaderParameter("Content-Type", "application/json;charset=UTF-8");
        request.addQueryParameter("accesstoken", token);
        int pageNum = 1;
        while (true) {
            JSONObject json = new JSONObject();
            json.put("pageSize", 100);
            json.put("pageNum", pageNum);
            JSONObject updateDate = new JSONObject();
            Long[] dateRange = new Long[2];
            DateTime.dateTimestamp(dateRange, new Date(), -1);
            updateDate.put("updateStartTime", dateRange[0]);
            updateDate.put("updateEndTime", dateRange[1]);
            json.put("queryParameter", updateDate);
            request.setJsonBody(json.toString());
            try {
                ApiExplorerResponse response = client.sendRequest(request);
                JSONObject body = JSONObject.parseObject(response.getResult());
                if (responseFail(body)) return "订单同步失败";
                JSONObject data = body.getJSONObject("data");
                JSONArray orderList = data.getJSONArray("pageList");
                for (int i = 0; i < orderList.size(); i++) {
                    JSONObject item = orderList.getJSONObject(i);
                    String id = "02_" + item.getLong("orderNo");
                    List<Order> list = orderDao.queryByOrderNum(id);
                    Order order = list.size() > 0 ? list.get(0) : new Order();
                    order.setOrderNum(id);
                    order.setCustomerName(item.getString("userNickname"));
                    order.setOriginPrice(item.getBigDecimal("goodsAmount"));
                    order.setLastPrice(item.getBigDecimal("paymentAmount"));
                    order.setReceivedAmount(item.getBigDecimal("paymentAmount"));
                    order.setPayTime(item.getString("paymentTime"));
                    order.setSaleChannel(item.getString("channelTypeName"));
                    order.setOrderSource("微盟");
                    int[] orderStatusShift = new int[]{0, 1, 1, 2, -1, 2, 2};
                    int orderStatus = item.getInteger("orderStatus");
                    order.setOrderStatus(orderStatus > 6 ? 2 : orderStatusShift[orderStatus]);
                    if (list.size() > 0) {
                        orderDao.updateById(order);
                    } else {
                        orderDao.insert(order);
                    }

                    list = orderDao.queryByOrderNum(id);
                    JSONArray itemList = item.getJSONArray("itemList");
                    if (itemList == null) continue;
                    for (int j = 0; j < itemList.size(); j++) {
                        JSONObject product = itemList.getJSONObject(j);
                        String pid = "02_" + product.getLong("goodsId");
                        List<Product> productList = productDao.queryBySourceId(pid);
                        if (productList.size() == 0) continue;
                        QueryWrapper<OrderProduct> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("order_id", list.get(0).getId());
                        queryWrapper.eq("product_id", productList.get(0).getId());
                        List<OrderProduct> relation = orderProductDao.selectList(queryWrapper);
                        OrderProduct orderProduct = relation.size() > 0 ? relation.get(0) : new OrderProduct();
                        orderProduct.setOrderId(list.get(0).getId());
                        orderProduct.setProductId(productList.get(0).getId());
                        orderProduct.setProductName(product.getString("goodsTitle"));
                        orderProduct.setProductImage(product.getString("imageUrl"));
                        orderProduct.setProductAmount(product.getInteger("skuNum"));
                        orderProduct.setOriginPrice(product.getBigDecimal("totalAmount"));
                        orderProduct.setLastPrice(product.getBigDecimal("totalAmount"));
                        orderProduct.setReceivedAmount(product.getBigDecimal("paymentAmount"));
                        if (relation.size() > 0) {
                            orderProductDao.updateById(orderProduct);
                        } else {
                            orderProductDao.insert(orderProduct);
                        }
                    }
                }
                int totalCount = data.getInteger("totalCount");
                if (pageNum * 100 >= totalCount) break;
                pageNum++;
            } catch (Exception e) {
                e.printStackTrace();
                return "订单同步失败";
            }
        }
        return null;
    }

    @Override
    public JSONObject queryUserInfo(Long userId) {
        String token = getToken();
        if (token == null) return null;
        ApiExplorerClient client = new ApiExplorerClient(new AppSigner());
        String path = "https://dopen.weimob.com/api/1_0/ec/membership/queryUserInfoOpen";
        ApiExplorerRequest request = new ApiExplorerRequest(HttpMethodName.POST, path);
        request.addHeaderParameter("Content-Type", "application/json;charset=UTF-8");
        request.addQueryParameter("accesstoken", token);
        JSONObject json = new JSONObject();
        json.put("wid", userId);
        json.put("StoreId", 0);
        request.setJsonBody(json.toString());

        try {
            ApiExplorerResponse response = client.sendRequest(request);
            JSONObject body = JSONObject.parseObject(response.getResult());
            if (responseFail(body)) return null;
            JSONObject data = body.getJSONObject("data");
            return data.getJSONObject("customerInfo");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String distributeUrl(Long userId) {
        String token = getToken();
        if (token == null) return null;
        ApiExplorerClient client = new ApiExplorerClient(new AppSigner());
        String path = "https://dopen.weimob.com/api/1_0/newsdp/weike/getWeikeUrl";
        ApiExplorerRequest request = new ApiExplorerRequest(HttpMethodName.POST, path);
        request.addHeaderParameter("Content-Type", "application/json;charset=UTF-8");
        request.addQueryParameter("accesstoken", token);
        JSONObject json = new JSONObject();
        json.put("wid", userId);
        request.setJsonBody(json.toString());

        try {
            ApiExplorerResponse response = client.sendRequest(request);
            JSONObject body = JSONObject.parseObject(response.getResult());
            if (responseFail(body)) return null;
            JSONObject data = body.getJSONObject("data");
            JSONObject shopUrl = data.getJSONObject("shopUrl");
            return shopUrl.getString("h5Url");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取access-token，用redis缓存
     */
    private String getToken() {
        String token = stringRedisTemplate.opsForValue().get("weimob");
        if (token == null) {
            ApiExplorerClient client = new ApiExplorerClient(new AppSigner());

            String path = "https://dopen.weimob.com/fuwu/b/oauth2/token";
            ApiExplorerRequest request = new ApiExplorerRequest(HttpMethodName.POST, path);
            request.addHeaderParameter("Content-Type", "application/json;charset=UTF-8");
            request.addQueryParameter("grant_type", "client_credentials");
            request.addQueryParameter("client_id", clientId);
            request.addQueryParameter("client_secret", clientSecret);
            try {
                ApiExplorerResponse response = client.sendRequest(request);
                JSONObject body = JSONObject.parseObject(response.getResult());
                token = body.getString("access_token");
                stringRedisTemplate.opsForValue().set(
                        "weimob",
                        token,
                        body.getLong("expires_in") - 50,
                        TimeUnit.SECONDS
                );
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return token;
    }

    /**
     * 检查错误码
     */
    private boolean responseFail(JSONObject body) {
        if (body == null) return true;
        JSONObject code = body.getJSONObject("code");
        if (code == null) return true;
        String errcode = code.getString("errcode");
        if (errcode == null || !errcode.equals("0")) return true;
        String errmsg = code.getString("errmsg");
        return errmsg == null || !errmsg.equals("处理成功");
    }
}
