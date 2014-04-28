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
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
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
        adminbar.setDisplayName(ChatColor.YELLOW + "Admin Bar");
        adminbar.setDisplaySlot(DisplaySlot.SIDEBAR);
        task = new UpdateScoreboard().runTaskTimer(this, 0, 20); // Update scoreboard every second
        this.getServer().getPluginManager().registerEvents(new AdminListener(), this);
        for (Player pl : Bukkit.getOnlinePlayers()) {
            if (pl.hasPermission("adminbar.view")) {
                pl.setScoreboard(sb);
            }
        }
    }

    public void onDisable() {
        for (Player pl : Bukkit.getOnlinePlayers()) {
            if (pl.hasPermission("adminbar.view")) {
                pl.setScoreboard(null);
            }
        }
    }

    /**
     * Get the current TPS.
     * @param ago When was this script last called? Should be 100 ticks! (~5 seconds)
     * @return The ticks per second rate, rounded to no decimals.
     */
    public static int getTPS(long ago) {
        long now = System.currentTimeMillis();
        double tps = (20000/(now - ago)); // 20 ticks in 1 second. We're dealing with milliseconds so multiply both by 1000. 20000 ticks and 1000 ms. Divide the ticks by the ms to get ticks per second
        System.out.println(tps);
        return (int)Math.round(tps);
    }

    /**
     * Get online players
     * @return The number of online players.
     */
    public static int getOnline() {
        return Bukkit.getServer().getOnlinePlayers().length;
    }

    public static int getOnlineStaff() {
        int staff = 0;
        for (Player pl : Bukkit.getServer().getOnlinePlayers()) {
            if (pl.hasPermission("adminbar.staff")) {
                staff++;
            }
        }
        return staff;
    }

    /**
     * Gets the used RAM.
     * @return Used RAM in mebibytes (bytes/(1024*1024)) - base 2.
     */
    public static int getUsedRAM() {
        Runtime rt = Runtime.getRuntime();
        long max = rt.maxMemory();
        long free = rt.freeMemory();
        long used = max - free;
        int mib = (int)(used / (1024*1024)); // doubt we have to worry about overflows here... 2^31-1 MiB is ~2.2 TiB!
        return mib;
    }

    /**
     * Gets the percentage of RAM used.
     * @return The percentage of RAM used (will always be 0-100)
     */
    public static int getRAMPercent() {
        Runtime rt = Runtime.getRuntime();
        long max = rt.maxMemory();
        long free = rt.freeMemory();
        int percent = (int)(free*100/max); // Can't end up with anything more than 100 and less than 0
        return 100-percent; // Get USED RAM instead of remaining RAM
    }
}
