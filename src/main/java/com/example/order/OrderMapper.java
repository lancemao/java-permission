package com.example.order;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OrderMapper {
    @Insert("INSERT INTO orders(user_id, name) VALUES(#{user_id}, #{name})")
    void insert(Order order);

    @Select("SELECT * FROM orders WHERE user_id = #{user_id}")
    List<Order> getAll(String user_id);

    @Delete("DELETE FROM orders WHERE idorder = #{id}")
    void delete(int id);
}
