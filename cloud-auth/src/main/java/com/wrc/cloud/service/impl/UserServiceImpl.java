package com.wrc.cloud.service.impl;

import com.wrc.cloud.entity.User;
import com.wrc.cloud.dao.UserDao;
import com.wrc.cloud.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;
import java.math.BigInteger;

/**
 * (TUser)表服务实现类
 *
 * @author makejava
 * @since 2023-02-06 21:15:29
 */
@Service("tUserService")
public class UserServiceImpl implements UserService {
    @Resource
    private UserDao tUserDao;

    /**
     * 通过ID查询单条数据
     *
     * @param uid 主键
     * @return 实例对象
     */
    @Override
    public User queryById(BigInteger uid) {
        return this.tUserDao.queryById(uid);
    }

    @Override
    public User queryByUsername(String username) {
        return tUserDao.queryByUsername(username);
    }

    /**
     * 分页查询
     *
     * @param user       筛选条件
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @Override
    public Page<User> queryByPage(User user, PageRequest pageRequest) {
        long total = this.tUserDao.count(user);
        return new PageImpl<>(this.tUserDao.queryAllByLimit(user, pageRequest), pageRequest, total);
    }

    @Override
    public Long count(User condition) {
        return tUserDao.count(condition);
    }

    /**
     * 新增数据
     *
     * @param user 实例对象
     * @return 实例对象
     */
    @Override
    public User insert(User user) {
        this.tUserDao.insert(user);
        return user;
    }

    /**
     * 修改数据
     *
     * @param user 实例对象
     * @return 实例对象
     */
    @Override
    public User update(User user) {
        this.tUserDao.update(user);
        return this.queryById(user.getUid());
    }

    /**
     * 通过主键删除数据
     *
     * @param uid 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(BigInteger uid) {
        return this.tUserDao.deleteById(uid) > 0;
    }
}
