package com.agentforge.tool.builtin;

import com.agentforge.tool.annotation.Tool;
import com.agentforge.tool.annotation.ToolParam;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

/**
 * 日期时间工具
 */
public class DateTimeTool {

    @Tool(name = "get_current_time", description = "获取当前日期和时间")
    public String getCurrentTime(
            @ToolParam(description = "时区，例如: Asia/Shanghai, UTC，默认为系统时区", required = false) String timezone) {
        try {
            ZoneId zoneId;
            if (timezone != null && !timezone.isBlank()) {
                zoneId = ZoneId.of(timezone);
            } else {
                zoneId = ZoneId.systemDefault();
            }
            LocalDateTime now = LocalDateTime.now(zoneId);
            return now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + " (" + zoneId + ")";
        } catch (Exception e) {
            return "错误: " + e.getMessage();
        }
    }

    @Tool(name = "format_datetime", description = "格式化日期时间字符串")
    public String formatDatetime(
            @ToolParam(description = "日期时间字符串，格式: yyyy-MM-dd HH:mm:ss") String datetime,
            @ToolParam(description = "输出格式，例如: yyyy年MM月dd日 HH:mm, MM/dd/yyyy") String format) {
        try {
            LocalDateTime ldt = LocalDateTime.parse(datetime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            return ldt.format(DateTimeFormatter.ofPattern(format));
        } catch (Exception e) {
            return "格式化错误: " + e.getMessage();
        }
    }

    @Tool(name = "calculate_duration", description = "计算两个时间点之间的时长")
    public String calculateDuration(
            @ToolParam(description = "开始时间，格式: yyyy-MM-dd HH:mm:ss") String start,
            @ToolParam(description = "结束时间，格式: yyyy-MM-dd HH:mm:ss") String end) {
        try {
            LocalDateTime startTime = LocalDateTime.parse(start, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            LocalDateTime endTime = LocalDateTime.parse(end, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            long seconds = java.time.Duration.between(startTime, endTime).getSeconds();
            long days = seconds / 86400;
            long hours = (seconds % 86400) / 3600;
            long minutes = (seconds % 3600) / 60;
            long secs = seconds % 60;

            return String.format("%d天 %d小时 %d分钟 %d秒", days, hours, minutes, secs);
        } catch (Exception e) {
            return "计算错误: " + e.getMessage();
        }
    }
}
