package com.cts.wealthmanagementsystem.repository;

import com.cts.wealthmanagementsystem.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Integer> {
}