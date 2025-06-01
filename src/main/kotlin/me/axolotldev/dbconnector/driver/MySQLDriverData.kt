/*
*
* Copyright (C) 2025 AxolotlDev and the MySQLDriver contributors
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program. If not, see <https://www.gnu.org/licenses/>.
*
*/

package me.axolotldev.dbconnector.driver

import me.axolotldev.dbconnector.abstracts.database.DatabaseInfo
import me.axolotldev.dbconnector.abstracts.database.DriverInfo
import java.sql.DriverManager

/**
 * MySQLDriverData represents the MySQL database driver information.
 * It extends the DriverInfo abstract class and provides the necessary
 * details and functions for MySQL database connections.
 */
class MySQLDriverData: DriverInfo {

    companion object {
        /**
         * Singleton instance of MySQLDriverData for global use.
         */
        @JvmField
        @Suppress("unused")
        val INSTANCE: MySQLDriverData = MySQLDriverData()
    }

    private constructor()

    /**
     * Gets the driver name, which is fixed as "MySQLDriver".
     */
    override val name: String
        get() = "MySQLDriver"

    /**
     * Gets the fully qualified class name of the MySQL JDBC driver.
     */
    override val driverAddress: String
        get() = "com.mysql.cj.jdbc.Driver"

    /**
     * Generates a JDBC connection URI string for MySQL using the given DatabaseInfo.
     *
     * @param info the DatabaseInfo object containing host, port, database name,
     *             and additional connection options
     * @return a formatted JDBC URI string in the form of
     *         jdbc:mysql://host:port/database?option1=value1&option2=value2
     */
    override fun generateURI(info: DatabaseInfo): String {
        val host = info.uri
        val port = info.port ?: 3306
        val database = info.database ?: ""

        val options = info.connectionOption
            .filterValues { it != null }
            .map { "${it.key}=${it.value}" }
            .joinToString("&")

        return buildString {
            append("jdbc:mysql://")
            append("$host:$port")
            if (database.isNotEmpty()) append("/$database")
            if (options.isNotEmpty()) append("?$options")
        }
    }

    /**
     * Checks whether the given JDBC URI is valid and accepted by any loaded JDBC driver.
     *
     * @param uri the JDBC URI string to validate
     * @return true if any registered driver accepts the URI, false otherwise
     */
    override fun isValidURI(uri: String): Boolean {
        return try {
            DriverManager.getDrivers().asSequence().any { it.acceptsURL(uri) }
        } catch (_: Exception) {
            false
        }
    }

}
