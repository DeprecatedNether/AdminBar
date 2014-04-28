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
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

public class AdminBar extends JavaPlugin {

    public static Objective adminbar;
    public static Scoreboard sb;
    BukkitTask task;

    public void onEnable() {
        ScoreboardManager sbm = Bukkit.getServer().getScoreboardManager();
        sb = sbm.getNewScoreboard();
        adminbar = sb.registerNewObjective("adminBar", "dummy");
        adminbar.setDisplayName("Admin Bar");
        adminbar.setDisplaySlot(DisplaySlot.SIDEBAR);
        task = new UpdateScoreboard().runTaskTimer(this, 0, 100); // Update scoreboard every 5 seconds (5*20)
        this.getServer().getPluginManager().registerEvents(new AdminListener(), this);
    }

    public void onDisable() {

    }

    /**
     * Get the current TPS.
     * @param ago When was this script last called? Should be 100 ticks! (~5 seconds)
     * @return The ticks per second rate, rounded to no decimals.
     */
    public static int getTPS(long ago) {
        long now = System.currentTimeMillis();
        double tps = (100000/(now - ago)); // 100 ticks in 5 seconds. We're dealing with milliseconds so multiply both by 1000. 100000 ticks and 5000 ms. Divide the ticks by the ms to get ticks per second
        return (int)Math.round(tps);
    }

    /**
     * Get online players
     * @return The number of online players.
     */
    public static int getOnline() {
        return Bukkit.getServer().getOnlinePlayers().length;
    }
}
