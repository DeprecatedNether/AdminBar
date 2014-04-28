/*
 * AdminBar is a Bukkit plugin that displays server information to administrators at all times.
 * Copyright (C) 2014 DeprecatedNether
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
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package pw.deprecatednether.adminbar;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

public class UpdateScoreboard extends BukkitRunnable {

    long lastrun = -1;

    @Override
    public void run() {
        Objective adminbar = AdminBar.adminbar;
        Score tps = adminbar.getScore(Bukkit.getServer().getOfflinePlayer("Server TPS"));
        Score players = adminbar.getScore(Bukkit.getServer().getOfflinePlayer("Online Players"));
        if (lastrun != -1) {
            tps.setScore(AdminBar.getTPS(lastrun));
        }
        lastrun = System.currentTimeMillis();
        players.setScore(AdminBar.getOnline());
    }
}
