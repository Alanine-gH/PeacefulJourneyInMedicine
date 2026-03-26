//package com.PJDM.listener;
//
//import jakarta.annotation.PostConstruct;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.util.concurrent.atomic.AtomicBoolean;
//
///**
// * 系统启动监听器
// *
// * @author kongshuo
// * @since 2025-12-25
// */
//@Component  // 启用菜单数据初始化
//public class StartupListener {
//
//    // 使用静态AtomicBoolean确保只执行一次
//    private static final AtomicBoolean INITIALIZED = new AtomicBoolean(false);
//
//    // 使用@PostConstruct注解，确保只执行一次
//    @PostConstruct
//    public void init() {
//        System.out.println("[DEBUG] StartupListener.init() called, INITIALIZED: " + INITIALIZED.get());
//        // 使用AtomicBoolean确保只执行一次
//        if (INITIALIZED.compareAndSet(false, true)) {
//            System.out.println("======= 系统启动，开始初始化菜单数据 =======");
//            try {
//               // menuService.initSystemMenus();
//            } catch (Exception e) {
//                System.err.println("菜单初始化失败: " + e.getMessage());
//                e.printStackTrace();
//            }
//        }
//    }
//}
