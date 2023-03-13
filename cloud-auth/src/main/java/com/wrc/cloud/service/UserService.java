package com.wrc.cloud.service;

import com.wrc.cloud.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.math.BigInteger;

/**
 * (TUser)表服务接口
 *
 * @author makejava
 * @since 2023-02-06 21:15:27
 */
public interface UserService {

    /**
     * 通过ID查询单条数据
     *
     * @param uid 主键
     * @return 实例对象
     */
    User queryById(BigInteger uid);

    User queryByUsername(String username);


    /**
     * 分页查询
     *
     * @param user       筛选条件
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    Page<User> queryByPage(User user, PageRequest pageRequest);


    public Long count(User condition);

    /**
     * 新增数据
     *
     * @param user 实例对象
     * @return 实例对象
     */
    User insert(User user);

    /**
     * 修改数据
     *
     * @param user 实例对象
     * @return 实例对象
     */
    User update(User user);

    /**
     * 通过主键删除数据
     *
     * @param uid 主键
     * @return 是否成功
     */
    boolean deleteById(BigInteger uid);

}
