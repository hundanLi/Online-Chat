package club.lzlbog.chatting.controller;

import com.alibaba.druid.stat.DruidStatManagerFacade;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * @author li
 * @version 1.0
 * @date 2019-03-17 16:38
 **/
@ApiIgnore
@RestController
public class DruidStatController {

    @PreAuthorize("hasAuthority('admin')")
    @GetMapping("/druid/stat")
    public Object druidStat(){
        // DruidStatManagerFacade#getDataSourceStatDataList 该方法可以获取所有数据源的监控数据，除此之外 DruidStatManagerFacade 还提供了一些其他方法，你可以按需选择使用。
        return DruidStatManagerFacade.getInstance().getDataSourceStatDataList();
    }
}
