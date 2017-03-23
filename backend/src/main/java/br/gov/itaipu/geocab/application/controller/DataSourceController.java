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
    public
    @ResponseBody
    List<DataSource> getDataSources() {
        return this.dataSourceService.listAllDataSource();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    @PreAuthorize("hasRole('admin')")
    public
    @ResponseBody
    DataSource getDataSource(@PathVariable long id) {
        return this.dataSourceService.findDataSourceById(id);
    }

    @RequestMapping(method = RequestMethod.POST)
    @PreAuthorize("hasRole('admin')")
    public
    @ResponseBody
    DataSource createDataSource(@RequestBody DataSource dataSource) {
        // remove o ID para evitar problemas
        dataSource.setId(0L);
        return this.dataSourceService.insertDataSource(dataSource);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
    @PreAuthorize("hasRole('admin')")
    public
    @ResponseBody
    DataSource updateDataSource(@PathVariable long id,
                                @RequestBody DataSource dataSource) {
        // ajusta o ID passado
        dataSource.setId(id);
        return this.dataSourceService.updateDataSource(dataSource);
    }
}
