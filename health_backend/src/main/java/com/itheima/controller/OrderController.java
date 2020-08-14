package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.service.OrderService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Reference
    private OrderService orderService;

    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        PageResult pageResult = orderService.pageQuery(
                queryPageBean.getCurrentPage(),
                queryPageBean.getPageSize(),
                queryPageBean.getQueryString()
        );
        return pageResult;
    }

    // <tr v-for="o in tableData">
//                                            <td>
//                                                <input :id="o.id" v-model="setmealIds" type="checkbox" :value="o.id">
//                                            </td>
//                                            <td><label :for="o.id">{{o.name}}</label></td>
//                                            <td><label :for="o.id">{{o.sex == '0' ? '不限' : o.sex == '1' ? '男' :
//            '女'}}</label></td>
//                                            <td><label :for="o.id">{{o.remark}}</label></td>
//                                        </tr>
//                                        </tbody>
//    age	"98+9"
//    birthday	"2020-08-15"
//    idCard	"++89"
//    marry	"0"
//    name	"8998"
//    orderDate	"2020-08-10"
//    setmealId	[…]
//            0	6
//            1	7
//            2	9
//    sex	"1"
//    telephone	"98+98"
    @RequestMapping("/add")
    public Result add(@RequestBody Map data) {

        try {
          Result result=  orderService.add(data);
            return new Result(true, MessageConstant.ORDER_SUCCESS);
        } catch (Exception e) {
            return  new Result(false, MessageConstant.ORDERSETTING_FAIL);
        }
    }

}
