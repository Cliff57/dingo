/*
 * Copyright 2021 DataCanvas
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.dingodb.web.service;

import io.dingodb.web.model.dto.Stmt;
import io.dingodb.web.model.vo.ResVo;
import io.dingodb.web.repo.CommonRepo;
import io.dingodb.web.repo.StmtRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;
import javax.annotation.Resource;

@Slf4j
@Service
public class DaoService {

    @Value("${server.host}")
    private String host;

    @Resource
    private StmtRepo stmtRepo;

    @Resource
    private CommonRepo commonRepo;

    public List<Stmt> findAll() {
        commonRepo.executeSQL("set names gbk");
        return stmtRepo.topStmtSql();
    }

    public List<Stmt> topSql() {
        commonRepo.executeSQL("set names gbk");
        return stmtRepo.topSql();
    }

    public ResVo login(String user, String password) {
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Properties props = new Properties();
            props.setProperty("user", user);
            props.setProperty("password", password);
            connection = DriverManager.getConnection("jdbc:mysql://" + host + ":3307/dingo", props);
            statement = connection.createStatement();
            rs = statement.executeQuery("show grants");
            if (rs.next()) {
                String grants = rs.getString(1);
                if (grants.contains("MONITOR")) {
                    return new ResVo(1, "", 1);
                }
            }

            return new ResVo(1, "", 2);
        } catch (Exception e) {
            return new ResVo(0, "", 2);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ignored) {
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ignored) {
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception ignored) {
                }
            }
        }
    }
}
