package services;

import play.Logger;
import play.inject.ApplicationLifecycle;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Clock;
import java.time.Instant;
import java.util.Date;
import java.util.Timer;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

/**
 * This class demonstrates how to run code when the
 * application starts and stops. It starts a timer when the
 * application starts. When the application stops it prints out how
 * long the application was running for.
 *
 * This class is registered for Guice dependency injection in the
 *  class. We want the class to start when the application
 * starts, so it is registered as an "eager singleton". See the code
 * in the  class to see how this happens.
 *
 * This class needs to run code when the server stops. It uses the
 * application's {@link ApplicationLifecycle} to register a stop hook.
 */
@Singleton
public class ApplicationTimer {

    private final Clock clock;
    private final ApplicationLifecycle appLifecycle;
    private final Instant start;


    @Inject
    public ApplicationTimer(Clock clock, ApplicationLifecycle appLifecycle) {
        this.clock = clock;
        this.appLifecycle = appLifecycle;
        // This code is called when the application starts.
        start = clock.instant();
        Logger.info("ApplicationTimer demo: Starting application at " + start);
        int minute = 1000*60;
        int second = 1000;

        // 一天的毫秒数
        long daySpan = 24 * 60 * 60 * 1000;
        // 规定的每天运行时间凌晨3点
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd '03:00:00'");
        // 首次运行时间
        Date startTime = null;
        try {
            startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(sdf.format(new Date()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // 如果今天的已经过了 首次运行时间就改为明天
        if(System.currentTimeMillis() > startTime.getTime())
            startTime = new Date(startTime.getTime() + daySpan);

        Timer timer1 = new Timer();
        Timer timer2 = new Timer();
        Timer timer3 = new Timer();
        Timer timer4 = new Timer();

        Timer timer5 = new Timer();
        Timer timer6 = new Timer();
        Timer timer7 = new Timer();
        Timer timer8 = new Timer();
        Timer timer9 = new Timer();

        //每天统计一次pv/uv
        timer1.scheduleAtFixedRate(new PvUvDateCount(), startTime, daySpan);
        //每5分钟获取一次百度热词新闻
        timer2.schedule(new GetHotNews(), minute*1, minute*10);
        //每天统计一次点击量
        timer3.scheduleAtFixedRate(new NewsClickCount(), startTime, daySpan);
        //每小时更新公共新闻池
        timer4.schedule(new UpdateNewsFeedCommonPerHour(), minute*1, minute*60);

        //每5分钟处理一次待推荐的通知
        timer5.schedule(new RecommendData(0, 5), second, second*30);
        timer6.schedule(new RecommendData(1, 5), second*10, second*30);
        timer7.schedule(new RecommendData(2, 5), second*15, second*30);
        timer8.schedule(new RecommendData(3, 5), second*20, second*30);
        timer9.schedule(new RecommendData(4, 5), second*25, second*30);


        // When the application starts, register a stop hook with the
        // ApplicationLifecycle object. The code inside the stop hook will
        // be run when the application stops.
        appLifecycle.addStopHook(() -> {
            Instant stop = clock.instant();
            Long runningTime = stop.getEpochSecond() - start.getEpochSecond();
            Logger.info("ApplicationTimer demo: Stopping application at " + clock.instant() + " after " + runningTime + "s.");
            return CompletableFuture.completedFuture(null);
        });
    }

}
