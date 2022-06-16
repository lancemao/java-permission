package com.example.order;

import cn.authing.permission.permission.RequiresResource;
import com.alibaba.fastjson.JSON;
import cn.authing.permission.core.BaseResponse;
import cn.authing.permission.core.UserInfo;
import cn.authing.permission.permission.RequiresRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("order")
public class OrderController {

    @Autowired
    OrderMapper mapper;

    @RequiresRole("admin")
    @ResponseBody
    @RequestMapping("/create")
    public String create(HttpServletRequest request, HttpServletResponse response,
                         @RequestBody Order order) throws IOException {
        UserInfo userInfo = (UserInfo) request.getAttribute("UserInfo");
        order.setUser_id(userInfo.getId());
        mapper.insert(order);
        return JSON.toJSONString(new BaseResponse<>());
    }

    @RequiresResource("order:read")
    @GetMapping("/list")
    public String list(HttpServletRequest request, HttpServletResponse response) {
        UserInfo userInfo = (UserInfo) request.getAttribute("UserInfo");
        List<Order> orders = mapper.getAll(userInfo.getId());
        BaseResponse<List<Order>> res = new BaseResponse<>();
        res.setData(orders);
        return JSON.toJSONString(res);
    }

    @RequiresRole("admin")
    @DeleteMapping("/delete")
    public String delete(HttpServletRequest request, HttpServletResponse response, @RequestParam("id")int orderId) {
        UserInfo userInfo = (UserInfo) request.getAttribute("UserInfo");
        List<Order> orders = mapper.getAll(userInfo.getId());
        for (Order order : orders) {
            if (order.getIdorder() == orderId) {
                mapper.delete(orderId);
                return JSON.toJSONString(new BaseResponse<>());
            }
        }

        response.setStatus(403);
        return "Order id is invalid";
    }
}
