package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.constant.MessageConstant;
import com.itheima.dao.MemberDao;
import com.itheima.dao.OrderDao;
import com.itheima.dao.OrderSettingDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.Result;
import com.itheima.pojo.Member;
import com.itheima.pojo.Order;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrderService;
import com.itheima.utils.DateUtils;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.crypto.Data;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = OrderService.class)
@Transactional
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderSettingDao orderSettingDao;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private OrderDao orderDao;

    @Override
    public Result order(Map map) throws Exception {
//        检查当前日期是否已经提前设置
        String orderDate = (String) map.get("orderDate");//获取当前预约日期
        OrderSetting orderSetting = orderSettingDao.findByOrderDate(DateUtils.parseString2Date(orderDate));
        if (orderSetting == null) {
//            返回日期错误
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }
//检查用户所预约日期是否已经预约满
        int number = orderSetting.getNumber();//预约上限
        int reservations = orderSetting.getReservations();//已预约人数
        if (reservations >= number) {
//            预约已满
            return new Result(false, MessageConstant.ORDER_FULL);
        }
//        判断是否重复预约
        //3、检查用户是否重复预约（同一个用户在同一天预约了同一个套餐），如果是重复预约则无法完成再次预约
        String telephone = (String) map.get("telephone");//获取用户页面输入的手机号
        Member member = memberDao.findByTelephone(telephone);
        if (member != null) {
            //判断是否在重复预约
            Integer memberId = member.getId();//会员ID
            Date order_Date = DateUtils.parseString2Date(orderDate);//预约日期
            String setmealId = (String) map.get("setmealId");//套餐ID
            Order order = new Order(memberId, order_Date, Integer.parseInt(setmealId));
            //根据条件进行查询
            List<Order> list = orderDao.findByCondition(order);
            if (list != null && list.size() > 0) {
                //说明用户在重复预约，无法完成再次预约
                return new Result(false, MessageConstant.HAS_ORDERED);
            }
        } else {
            //4、检查当前用户是否为会员，如果是会员则直接完成预约，如果不是会员则自动完成注册并进行预约
            member = new Member();
            member.setName((String) map.get("name"));
            member.setPhoneNumber(telephone);
            member.setIdCard((String) map.get("idCard"));
            member.setSex((String) map.get("sex"));
            member.setRegTime(new Date());
            memberDao.add(member);//自动完成会员注册
        }

        //5、预约成功，更新当日的已预约人数
        Order order = new Order();
        order.setMemberId(member.getId());//设置会员ID
        order.setOrderDate(DateUtils.parseString2Date(orderDate));//预约日期
        order.setOrderType((String) map.get("orderType"));//预约类型
        order.setOrderStatus(Order.ORDERSTATUS_NO);//到诊状态
        order.setSetmealId(Integer.parseInt((String) map.get("setmealId")));//套餐ID
        orderDao.add(order);

        orderSetting.setReservations(orderSetting.getReservations() + 1);//设置已预约人数+1
        orderSettingDao.editReservationsByOrderDate(orderSetting);
        return new Result(true, MessageConstant.ORDER_SUCCESS, order.getId());
    }

    @Override
    public PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString) {
        PageHelper.startPage(currentPage, pageSize);
        Page<Map<String, Object>> orders = orderDao.pageQuery(queryString);
        return new PageResult(orders.getTotal(), orders);
    }

    @Override
    public Map finyById(Integer id) throws Exception {
        Map map = orderDao.findById4Detail(id);
        if (map != null) {
            //更改日期格式
            Date date = (Date) map.get("orderDate");
            map.put("orderDate", DateUtils.parseDate2String(date));
        }
        return map;
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
    @Override
    public Result add(Map data) throws Exception {
        List<Integer> setmeaIds = (List<Integer>) data.get("setmealId");
        for (Integer setmeaId : setmeaIds) {
//          判断预约重复
            String orderDate = (String) data.get("orderDate");
//
            Date date = DateUtils.parseString2Date(orderDate);//日期
            OrderSetting byOrderDate = orderSettingDao.findByOrderDate(date);
            if (byOrderDate == null) {
                return new Result(false, MessageConstant.HAS_ORDERED);
            }


            //判断预约量
            int number = byOrderDate.getNumber();//总预约数
            int reservations = byOrderDate.getReservations();//当前预约数
            if (number <= reservations) {
                return new Result(false, MessageConstant.ORDER_FULL);
            }


            // 查询用户号码是否存在
            String telephone = (String) data.get("telephone");
            Member byTelephone = memberDao.findByTelephone(telephone);//id查询重复
            if (byTelephone != null) {
//               判断套餐用户id及创建日期是否重复
                Order order = new Order(byTelephone.getId(), date, setmeaId);//封装数据
                if (orderDao.findByCondition(order) != null) {
                    return new Result(false, MessageConstant.HAS_ORDERED);
                }
            } else {
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
                //用户不存在添加用户
                byTelephone = new Member();
                Map map = data;

                String name = String.valueOf(data.get("name"));
                if (name == null) {
                    return null;
                }
                byTelephone.setName((String) data.get("name"));
                byTelephone.setPhoneNumber(telephone);
                byTelephone.setIdCard((String) data.get("idCard"));
                byTelephone.setSex((String) data.get("sex"));
                byTelephone.setRegTime(new Date());
                memberDao.add(byTelephone);

            }


//            提交数据
            Order order = new Order();
            order.setMemberId(byTelephone.getId());//用户di
            order.setOrderDate(date);//预约日期
            order.setOrderStatus(Order.ORDERSTATUS_NO);//到诊状态
            order.setOrderType(Order.ORDERTYPE_TELEPHONE);//预约方式
            order.setSetmealId(setmeaId);//添加套餐id
            orderDao.add(order);

//            更新预约数
            int reservations1 = byOrderDate.getReservations();
            byOrderDate.setReservations(reservations1 + 1);
//            修改数据
            orderSettingDao.editNumberByOrderDate(byOrderDate);


        }
        return new Result(true, MessageConstant.ORDER_SUCCESS);
    }

}
