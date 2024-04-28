/*
 * Licensed to ObjectStyle LLC under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ObjectStyle LLC licenses
 * this file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package io.bootique.bom.jdbc;

import io.bootique.meta.application.CommandMetadata;
import io.bootique.meta.application.OptionMetadata;
import io.bootique.cli.Cli;
import io.bootique.command.CommandOutcome;
import io.bootique.command.CommandWithMetadata;
import io.bootique.jdbc.DataSourceFactory;
import io.bootique.log.BootLogger;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class RunSQLCommand extends CommandWithMetadata {

    @Inject
    private Provider<DataSourceFactory> dataSourceFactoryProvider;

    @Inject
    private BootLogger logger;

    public RunSQLCommand() {
        super(CommandMetadata.builder(RunSQLCommand.class)
                .addOption(OptionMetadata.builder("sql").valueRequired("sql_string").build())
                .build());
    }

    @Override
    public CommandOutcome run(Cli cli) {

        DataSource dataSource = dataSourceFactoryProvider.get().forName("test1");

        try (Connection c = dataSource.getConnection()) {
            prepareDB(c);

            for (String sql : cli.optionStrings("sql")) {
                runSELECT(c, sql);
            }

        } catch (SQLException ex) {
            logger.stderr("Error....", ex);
        }

        return CommandOutcome.succeeded();
    }

    private void prepareDB(Connection c) throws SQLException {

        try {
            runUPDATE(c, "CREATE TABLE T1 (ID INT PRIMARY KEY, NAME VARCHAR(200))");
        } catch (SQLException ex) {
            // see http://db.apache.org/derby/docs/10.8/ref/rrefexcept71493.html
            if ("X0Y32".equals(ex.getSQLState())) {
                logger.stdout("Table already exists...");
            } else {
                throw ex;
            }
        }

        runUPDATE(c, "DELETE FROM T1");
        runUPDATE(c, "INSERT INTO T1 VALUES (1, 'aa')");
        runUPDATE(c, "INSERT INTO T1 VALUES (2, 'bb')");
        c.commit();
    }

    private void runUPDATE(Connection c, String sql) throws SQLException {
        logger.stdout("Running update: " + sql);

        try (PreparedStatement st = c.prepareStatement(sql)) {
            st.executeUpdate();
        }
    }

    private void runSELECT(Connection c, String sql) throws SQLException {
        logger.stdout("Running select: " + sql);

        try (PreparedStatement st = c.prepareStatement(sql)) {

            try (ResultSet rs = st.executeQuery()) {
                ResultSetMetaData md = rs.getMetaData();

                while (rs.next()) {

                    StringBuffer line = new StringBuffer();
                    for (int i = 1; i <= md.getColumnCount(); i++) {
                        if (i > 1) {
                            line.append(",");
                        }
                        line.append(rs.getObject(i));
                    }

                    logger.stdout(line.toString());
                }
            }
        }
    }
}
