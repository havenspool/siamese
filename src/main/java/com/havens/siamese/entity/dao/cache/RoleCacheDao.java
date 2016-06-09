package com.havens.siamese.entity.dao.cache;

import com.havens.siamese.Constants;
import com.havens.siamese.db.DBException;
import com.havens.siamese.db.DataSourceManager;
import com.havens.siamese.db.KeyWords;
import com.havens.siamese.db.cache.DBObjectCacheDAO;
import com.havens.siamese.db.rs.MapToDBObject;
import com.havens.siamese.entity.Role;
import com.havens.siamese.entity.dao.RoleDao;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by havens on 16-5-14.
 */
public class RoleCacheDao extends DBObjectCacheDAO implements RoleDao {

    public RoleCacheDao() {
        super(DataSourceManager.getQueryRunner(KeyWords.MASTER),DataSourceManager.getQueryRunner(KeyWords.SLAVE));
    }

    private static RoleCacheDao innerDao = null;
    private static LoadingCache<Integer, List<Role>> userHeroes = CacheBuilder.newBuilder().maximumSize(Constants.MAX_CACHE_NUM_SIZE).build(new CacheLoader<Integer, List<Role>>() {
        public List<Role> load(Integer userId) throws Exception {
            if (innerDao == null) {
                innerDao = new RoleCacheDao();
            }
            return innerDao._getRoles(userId);
        }
    });

    private List<Role> _getRoles(Integer userId) {
        List<Role> roleList;
        try {
            roleList = slaveRunner.query("select * from roles where userId = ?", MapToDBObject.newListHandler(Role.class), userId);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<Role>();
        }
        return roleList;
    }

    private static LoadingCache<Integer, Role> allHeroes = CacheBuilder.newBuilder().maximumSize(Constants.MAX_CACHE_NUM_SIZE).build(new CacheLoader<Integer, Role>() {
        public Role load(Integer roleId) throws Exception {
            if (innerDao == null) {
                innerDao = new RoleCacheDao();
            }
            return innerDao._getRole(roleId);
        }
    });

    private Role _getRole(Integer roleId) {
        Role role=null;
        try {
            role = slaveRunner.query("select * from roles where id = ?", MapToDBObject.newRsHandler(Role.class), roleId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if ((role == null) || (role.id == 0)) {
            return null;
        }
        return role;
    }

    public Role getHero(int roleId) throws DBException{
        return allHeroes.getUnchecked(roleId);
    }

    public Role getHero(String roleName) throws DBException{
        Role role=null;
        try {
            role = slaveRunner.query("select * from roles where name = ?", MapToDBObject.newRsHandler(Role.class), roleName);
        } catch (SQLException e) {
            throw new DBException(e.getMessage());
        }
        return role;
    }

    public List<Role> getRoles(int userId) throws DBException {
        return userHeroes.getUnchecked(userId);
    }

    public Role createHero(int userId, int serverId, String roleName, int headImage) {
        Role role = new Role();
        try {
            role.userId = userId;
            role.serverId = serverId;
            role.roleName = roleName;
            role.headImage = headImage;
            role.createTime = System.currentTimeMillis()/1000;
            insert(role);
        } catch (DBException e) {
            e.printStackTrace();
            return null;
        }
        return role;
    }

    public void updateRole(Role role) throws DBException{
        update(role);
    }

    public void deleteRole(Role role) throws DBException{

    }

    public void deleteRoleById(int id) throws DBException{

    }
}
