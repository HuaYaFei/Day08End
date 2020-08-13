package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.service.ReportService;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service(interfaceClass = ReportService.class)
@Transactional
public class ReportServiceImpl implements ReportService {

    @Override
    public Map<String, Object> getBusinessReportData() {
        return null;
    }
}
