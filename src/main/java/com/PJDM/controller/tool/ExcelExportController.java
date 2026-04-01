package com.PJDM.controller.tool;

import com.PJDM.dto.*;
import com.PJDM.exception.ExcelExportException;
import com.PJDM.service.*;
import com.PJDM.untils.ExcelUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * Excel 导出控制器
 * 复用各模块 Service 的分页查询接口，强制 pageSize=10000 一次性导出
 *
 * @author Alanine
 */
@Slf4j
@RestController
@RequestMapping("/export")
@Tag(name = "Excel导出", description = "各模块数据导出为Excel文件")
public class ExcelExportController {

    @Autowired
    private ExcelUtil excelUtil;
    @Autowired
    private IOrderOrderService orderService;
    @Autowired
    private IMedicalHospitalService hospitalService;
    @Autowired
    private IMedicalExpertService expertService;
    @Autowired
    private IMedicalAppointmentService appointmentService;
    @Autowired
    private IMedicalTriageService triageService;
    @Autowired
    private IServicePackageService packageService;
    @Autowired
    private IServiceDispatchService dispatchService;

    // ==================== 订单 ====================

    @GetMapping("/order")
    @Operation(summary = "导出订单列表 Excel")
    public void exportOrder(OrderQueryDTO query, HttpServletResponse response) {
        log.info("[Excel导出] 订单列表 query={}", query);
        query.setPageNum(1);
        query.setPageSize(10000);
        IPage<?> page = orderService.getOrderListPage(query);
        doExport(response, page.getRecords(), "订单列表", "订单列表",
                buildHead(
                        "id", "ID",
                        "orderNo", "订单号",
                        "userName", "患者姓名",
                        "userPhone", "患者手机",
                        "accompanistName", "陪诊师",
                        "orderType", "订单类型",
                        "orderStatus", "订单状态",
                        "totalAmount", "总金额",
                        "currency", "货币",
                        "appointmentHospital", "预约医院",
                        "appointmentDate", "预约日期",
                        "createTime", "下单时间"
                ));
    }

    // ==================== 医院 ====================

    @GetMapping("/hospital")
    @Operation(summary = "导出医院列表 Excel")
    public void exportHospital(HospitalQueryDTO query, HttpServletResponse response) {
        log.info("[Excel导出] 医院列表 query={}", query);
        query.setPageNum(1);
        query.setPageSize(10000);
        IPage<?> page = hospitalService.getHospitalListPage(query);
        doExport(response, page.getRecords(), "医院列表", "医院列表",
                buildHead(
                        "id", "ID",
                        "hospitalCode", "医院编码",
                        "hospitalName", "医院名称",
                        "hospitalNameEn", "英文名称",
                        "hospitalType", "医院类型",
                        "city", "城市",
                        "phone", "联系电话",
                        "isCooperation", "是否合作",
                        "ratingScore", "评分",
                        "status", "状态",
                        "createTime", "创建时间"
                ));
    }

    // ==================== 专家 ====================

    @GetMapping("/expert")
    @Operation(summary = "导出专家列表 Excel")
    public void exportExpert(ExpertQueryDTO query, HttpServletResponse response) {
        log.info("[Excel导出] 专家列表 query={}", query);
        query.setPageNum(1);
        query.setPageSize(10000);
        IPage<?> page = expertService.getExpertListPage(query);
        doExport(response, page.getRecords(), "专家列表", "专家列表",
                buildHead(
                        "id", "ID",
                        "expertCode", "专家编码",
                        "expertName", "专家姓名",
                        "hospitalName", "所属医院",
                        "departmentName", "所属科室",
                        "professionalTitle", "职称",
                        "appointmentFee", "预约费用",
                        "isAcceptInternational", "接受国际患者",
                        "isRecommend", "是否推荐",
                        "ratingScore", "评分",
                        "status", "状态",
                        "createTime", "创建时间"
                ));
    }

    // ==================== 预约 ====================

    @GetMapping("/appointment")
    @Operation(summary = "导出预约列表 Excel")
    public void exportAppointment(AppointmentQueryDTO query, HttpServletResponse response) {
        log.info("[Excel导出] 预约列表 query={}", query);
        query.setPageNum(1);
        query.setPageSize(10000);
        IPage<?> page = appointmentService.getAppointmentListPage(query);
        doExport(response, page.getRecords(), "预约列表", "预约列表",
                buildHead(
                        "id", "ID",
                        "appointmentNo", "预约编号",
                        "userName", "患者姓名",
                        "userPhone", "患者手机",
                        "hospitalName", "医院",
                        "departmentName", "科室",
                        "expertName", "专家",
                        "appointmentDate", "预约日期",
                        "appointmentTime", "预约时间",
                        "appointmentType", "预约类型",
                        "appointmentStatus", "预约状态",
                        "createTime", "创建时间"
                ));
    }

    // ==================== 分诊 ====================

    @GetMapping("/triage")
    @Operation(summary = "导出分诊记录 Excel")
    public void exportTriage(TriageQueryDTO query, HttpServletResponse response) {
        log.info("[Excel导出] 分诊列表 query={}", query);
        query.setPageNum(1);
        query.setPageSize(10000);
        IPage<?> page = triageService.getTriageListPage(query);
        doExport(response, page.getRecords(), "分诊记录", "分诊记录",
                buildHead(
                        "id", "ID",
                        "triageNo", "分诊单号",
                        "userName", "患者姓名",
                        "userPhone", "患者手机",
                        "age", "年龄",
                        "chiefComplaint", "主诉",
                        "painLocation", "疼痛部位",
                        "painLevel", "疼痛等级",
                        "recommendedDepartment", "推荐科室",
                        "triageStatus", "分诊状态",
                        "aiConfidence", "AI置信度",
                        "createTime", "创建时间"
                ));
    }

    // ==================== 服务套餐 ====================

    @GetMapping("/package")
    @Operation(summary = "导出服务套餐 Excel")
    public void exportPackage(PackageQueryDTO query, HttpServletResponse response) {
        log.info("[Excel导出] 套餐列表 query={}", query);
        query.setPageNum(1);
        query.setPageSize(10000);
        IPage<?> page = packageService.getPackageListPage(query);
        doExport(response, page.getRecords(), "服务套餐", "服务套餐",
                buildHead(
                        "id", "ID",
                        "packageCode", "套餐编码",
                        "packageName", "套餐名称",
                        "packageType", "套餐类型",
                        "serviceType", "服务类型",
                        "price", "价格",
                        "currency", "货币",
                        "unit", "单位",
                        "duration", "时长(h)",
                        "validDays", "有效期(天)",
                        "status", "状态",
                        "createTime", "创建时间"
                ));
    }

    // ==================== 派单 ====================

    @GetMapping("/dispatch")
    @Operation(summary = "导出派单记录 Excel")
    public void exportDispatch(DispatchQueryDTO query, HttpServletResponse response) {
        log.info("[Excel导出] 派单列表 query={}", query);
        query.setPageNum(1);
        query.setPageSize(10000);
        IPage<?> page = dispatchService.getDispatchListPage(query);
        doExport(response, page.getRecords(), "派单记录", "派单记录",
                buildHead(
                        "id", "ID",
                        "dispatchNo", "派单编号",
                        "orderNo", "订单号",
                        "accompanistName", "陪诊师",
                        "accompanistPhone", "陪诊师手机",
                        "dispatchType", "派单类型",
                        "dispatchStatus", "派单状态",
                        "pushTime", "推送时间",
                        "receiveTime", "接单时间",
                        "rejectReason", "拒绝原因",
                        "createTime", "创建时间"
                ));
    }

    // ==================== 通用导出方法 ====================

    private void doExport(HttpServletResponse response, List<?> records,
                          String sheetName, String fileName, Map<String, String> headMap) {
        try {
            List<Map<String, Object>> dataList = new ArrayList<>();
            for (Object record : records) {
                Map<String, Object> row = new LinkedHashMap<>();
                for (String field : headMap.keySet()) {
                    try {
                        java.lang.reflect.Field f = record.getClass().getDeclaredField(field);
                        f.setAccessible(true);
                        Object val = f.get(record);
                        row.put(field, val != null ? val : "");
                    } catch (NoSuchFieldException ignored) {
                        row.put(field, "");
                    }
                }
                dataList.add(row);
            }
            log.info("[Excel导出] sheetName={} rows={}", sheetName, dataList.size());
            excelUtil.exportExcel(response, dataList, sheetName, fileName, headMap);
        } catch (Exception e) {
            log.error("[Excel导出] 失败 sheetName={} msg={}", sheetName, e.getMessage(), e);
            throw new ExcelExportException("导出 " + sheetName + " 失败", e);
        }
    }

    /**
     * 按顺序构建表头 LinkedHashMap（key=字段名, value=列标题）
     */
    private Map<String, String> buildHead(String... pairs) {
        Map<String, String> map = new LinkedHashMap<>();
        for (int i = 0; i < pairs.length - 1; i += 2) {
            map.put(pairs[i], pairs[i + 1]);
        }
        return map;
    }
}
