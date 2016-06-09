package com.havens.siamese.entity.dao;

import com.havens.siamese.db.DBException;
import com.havens.siamese.entity.Role;

import java.util.List;

/**
 * Created by havens on 16-5-14.
 */
public abstract interface RoleDao {
    Role getHero(int id) throws DBException;
    Role getHero(String name) throws DBException;
    List<Role> getRoles(int userId) throws DBException;
    Role createHero(int userId,int serverId,String roleName,int headImage);
    void updateRole(Role role) throws DBException;
    void deleteRole(Role role) throws DBException;
    void deleteRoleById(int id) throws DBException;
}
