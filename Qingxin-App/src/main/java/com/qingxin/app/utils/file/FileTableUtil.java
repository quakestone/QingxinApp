package com.qingxin.app.utils.file;


import com.qingxin.common.core.domain.CrmFile;
import com.qingxin.common.utils.uuid.IdUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class FileTableUtil {
    // 数据库连接信息
    private String DB_URL;
    private String DB_USERNAME;
    private String DB_PASSWORD;

    // 构造函数，通过注入方式获取数据库连接信息
    public FileTableUtil(
            @Value("${spring.datasource.url}") String dbUrl,
            @Value("${spring.datasource.username}") String dbUsername,
            @Value("${spring.datasource.password}") String dbPassword) {
        this.DB_URL = dbUrl;
        this.DB_USERNAME = dbUsername;
        this.DB_PASSWORD = dbPassword;
    }


    /**
     * 批量插入数据
     * @param fileEntries 文件对象集合
     * @throws SQLException
     */
    public void addFiles(List<CrmFile> fileEntries) throws SQLException {
        // SQL插入语句
        String query = "INSERT INTO crm_file (id, busi_id, busi_type, real_name, ten_name, path, format, size, create_user, create_time, update_user, update_time) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(query)) {

            for (CrmFile fileEntry : fileEntries) {
                fileEntry.setCreate();
                fileEntry.isBusiId(fileEntry.getBusiId());
                // 设置参数值
                statement.setString(1, IdUtils.simpleUUID());
                statement.setString(2, fileEntry.getBusiId());
                statement.setInt(3, fileEntry.getBusiType());
                statement.setString(4, fileEntry.getRealName());
                statement.setString(5, fileEntry.getTenName());
                statement.setString(6, fileEntry.getPath());
                statement.setString(7, fileEntry.getFormat());
                statement.setLong(8, fileEntry.getSize());
                statement.setString(9, fileEntry.getCreateUser());
                statement.setTimestamp(10, new Timestamp(fileEntry.getCreateTime().getTime()));
                statement.setString(11, null);
                statement.setTimestamp(12, null);

                // 添加到批处理
                statement.addBatch();
            }

            // 执行批处理
            int[] result = statement.executeBatch();

            // 检查批处理执行结果
            for (int i : result) {
                if (i == Statement.EXECUTE_FAILED) {
                    throw new SQLException("批量插入数据失败");
                }
            }
        }
    }

    /**
     * 修改文件对象
     * @param fileEntry
     * @throws SQLException
     */
    // 更新数据库中的文件记录
    public void updateFile(CrmFile fileEntry) throws SQLException {
        // SQL更新语句
        StringBuilder query = new StringBuilder("UPDATE crm_file SET");
        List<Object> paramValues = new ArrayList<>();

        // 构建更新字段和参数
        if (fileEntry.getBusiId() != null||fileEntry.getBusiId()=="") {
            query.append(" busi_id = ?,");
            paramValues.add(fileEntry.getBusiId());
        }
        if (!(fileEntry.getBusiType() == null)) {
            query.append(" busi_type = ?,");
            paramValues.add(fileEntry.getBusiType());
        }
        if (fileEntry.getRealName() != null||fileEntry.getRealName()=="") {
            query.append(" real_name = ?,");
            paramValues.add(fileEntry.getRealName());
        }
        if (fileEntry.getTenName() != null||fileEntry.getTenName()=="") {
            query.append(" ten_name = ?,");
            paramValues.add(fileEntry.getTenName());
        }
        if (fileEntry.getPath() != null||fileEntry.getPath()=="") {
            query.append(" path = ?,");
            paramValues.add(fileEntry.getPath());
        }
        if (fileEntry.getFormat() != null||fileEntry.getFormat()=="") {
            query.append(" format = ?,");
            paramValues.add(fileEntry.getFormat());
        }
        if (fileEntry.getSize() != null) {
            query.append(" size = ?,");
            paramValues.add(fileEntry.getSize());
        }
        // 移除最后一个逗号
        query.setLength(query.length() - 1);

        // 添加WHERE条件
        query.append(" WHERE id = ?");
        paramValues.add(fileEntry.getId());

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(query.toString())) {
            // 设置参数值
            for (int i = 0; i < paramValues.size(); i++) {
                statement.setObject(i + 1, paramValues.get(i));
            }

            // 执行更新操作
            statement.executeUpdate();
        }
    }

    /**
     * 批量删除
     * @param ids  文件id 集合
     * @throws SQLException
     */
    public void deleteFiles(List<String> ids) throws SQLException {
        // SQL删除语句
        String query = "DELETE FROM crm_file WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(query)) {
            for (String id : ids) {
                // 设置参数值
                statement.setString(1, id);

                // 添加到批处理
                statement.addBatch();
            }
            // 执行批处理
            int[] result = statement.executeBatch();

            // 检查批处理执行结果
            for (int i : result) {
                if (i == Statement.EXECUTE_FAILED) {
                    throw new SQLException("批量删除数据失败");
                }
            }
        }
    }

    /**
     * 批量查询
     * @param crmFile 查询条件对象
     * @return
     * @throws SQLException
     */

    public List<CrmFile> getFilesByParams(CrmFile crmFile) throws SQLException {
        // SQL查询语句
        StringBuilder query = new StringBuilder("SELECT * FROM crm_file");
        List<Object> paramValues = new ArrayList<>();

        if (crmFile != null) {
            query.append(" WHERE 1=1");

            if (crmFile.getId() != null && !crmFile.getId().equals("")) {
                query.append(" AND id = ?");
                paramValues.add(crmFile.getId());
            }

            if (crmFile.getBusiId() != null && !crmFile.getBusiId().equals("")) {
                query.append(" AND busi_id = ?");
                paramValues.add(crmFile.getBusiId());
            }

            if (crmFile.getBusiType() != null) {
                query.append(" AND busi_type = ?");
                paramValues.add(crmFile.getBusiType());
            }

            if (crmFile.getRealName() != null && !crmFile.getRealName().equals("")) {
                query.append(" AND real_name LIKE ?");
                paramValues.add("%" + crmFile.getRealName() + "%");
            }

            if (crmFile.getTenName() != null && !crmFile.getTenName().equals("")) {
                query.append(" AND ten_name LIKE ?");
                paramValues.add("%" + crmFile.getTenName() + "%");
            }

            if (crmFile.getPath() != null && !crmFile.getPath().equals("")) {
                query.append(" AND path LIKE ?");
                paramValues.add("%" + crmFile.getPath() + "%");
            }

            if (crmFile.getFormat() != null && !crmFile.getFormat().equals("")) {
                query.append(" AND format = ?");
                paramValues.add(crmFile.getFormat());
            }

            if (crmFile.getSize() != null) {
                query.append(" AND size = ?");
                paramValues.add(crmFile.getSize());
            }

            if (crmFile.getCreateUser() != null && !crmFile.getCreateUser().equals("")) {
                query.append(" AND create_user LIKE ?");
                paramValues.add("%" + crmFile.getCreateUser() + "%");
            }

            if (crmFile.getCreateTime() != null) {
                query.append(" AND create_time = ?");
                paramValues.add(crmFile.getCreateTime());
            }

            if (crmFile.getUpdateUser() != null && !crmFile.getUpdateUser().equals("")) {
                query.append(" AND update_user LIKE ?");
                paramValues.add("%" + crmFile.getUpdateUser() + "%");
            }

            if (crmFile.getUpdateTime() != null) {
                query.append(" AND update_time = ?");
                paramValues.add(crmFile.getUpdateTime());
            }
        }

        // 添加排序条件
        query.append(" ORDER BY create_time DESC");

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(query.toString())) {
            // 设置参数值
            for (int i = 0; i < paramValues.size(); i++) {
                statement.setObject(i + 1, paramValues.get(i));
            }

            try (ResultSet resultSet = statement.executeQuery()) {
                List<CrmFile> files = new ArrayList<>();
                while (resultSet.next()) {
                    files.add(mapResultSetToFileEntry(resultSet));
                }
                return files;
            }
        }
    }

    public List<String> getFilePathsByBusiId(String busiId) throws SQLException {
        // SQL查询语句
        String query = "SELECT path FROM crm_file WHERE busi_id = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(query)) {
            // 设置参数值
            statement.setString(1, busiId);

            try (ResultSet resultSet = statement.executeQuery()) {
                List<String> filePaths = new ArrayList<>();
                while (resultSet.next()) {
                    filePaths.add(resultSet.getString("path"));
                }
                return filePaths;
            }
        }
    }


    // 将ResultSet映射为CrmFile对象
    private CrmFile mapResultSetToFileEntry(ResultSet resultSet) throws SQLException {
        CrmFile fileEntry = new CrmFile();
        fileEntry.setId(resultSet.getString("id"));
        fileEntry.setBusiId(resultSet.getString("busi_id"));
        fileEntry.setBusiType(resultSet.getInt("busi_type"));
        fileEntry.setRealName(resultSet.getString("real_name"));
        fileEntry.setTenName(resultSet.getString("ten_name"));
        fileEntry.setPath(resultSet.getString("path"));
        fileEntry.setFormat(resultSet.getString("format"));
        fileEntry.setSize(resultSet.getInt("size"));
        fileEntry.setCreateUser(resultSet.getString("create_user"));
        fileEntry.setCreateTime(resultSet.getTimestamp("create_time"));
        fileEntry.setUpdateUser(resultSet.getString("update_user"));
        fileEntry.setUpdateTime(resultSet.getTimestamp("update_time"));

        return fileEntry;
    }
}
