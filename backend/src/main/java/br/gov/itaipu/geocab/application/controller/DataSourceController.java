package br.gov.itaipu.geocab.application.controller;

import br.gov.itaipu.geocab.domain.entity.datasource.DataSource;
import br.gov.itaipu.geocab.domain.service.DataSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by lcvmelo on 09/03/2017.
 */
@RestController
@RequestMapping("/api/data-source")
public class DataSourceController {

    @Autowired
    private DataSourceService dataSourceService;

    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("hasRole('admin')")
    public @ResponseBody List<DataSource> getDataSources() {
        return this.dataSourceService.listAllDataSource();
    }

    @RequestMapping(method = RequestMethod.POST)
    @PreAuthorize("hasRole('admin')")
    public @ResponseBody DataSource createDataSource(@RequestBody DataSource dataSource) {
        return this.dataSourceService.insertDataSource(dataSource);
    }
}
