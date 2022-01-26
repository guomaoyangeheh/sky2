package com.gmy.sky2.sqlscript.vip;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.file.FileWriter;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.gmy.sky2.sqlscript.vo.*;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;

import java.util.List;

/**
 * @Author guomaoyang
 * @Date 2021/4/30
 */
public class Program1 {

    // GMYTODO: 2021/4/30 2:56 下午 自贡诺奥汽车销售服务有限公司 和 五常市春诚汽车销售有限公司 的老合同id重复，这俩要单独的写sql
    private static String signSQL = "update b2b_vip_management_contract_sign set contract_id='%s',contract_no='%s',sign_time='%s',gmt_modified=NOW() where contract_id='%s';";
    private static String grantSQL = "update b2b_vip_management_grant set contract_id='%s',contract_sign_time='%s',gmt_modified=NOW() where  contract_id='%s';";
    private static String vipSQL = "update b2b_vip_management set open_time='%s',gmt_modified=NOW() where partner_name='%s' and status = 1;";

    // 合同id订正
    private static String signSQLConstractId = "update b2b_vip_management_grant set contract_id='%s' where  contract_id='%s';";
    private static String grantSQLConstractId = "update b2b_vip_management_contract_sign set contract_id='%s' where contract_id='%s';";

    // 修改开通时间为合同签约时间
    private static String vipOpenTime = "update b2b_vip_management set open_time = '%s', `gmt_modified` =now() where partner_name = '%s';";

    private static String sql1 = "update b2b_vip_management set effective_end_time = '%s', `gmt_modified` =now() where partner_name='%s';";
    private static String sql2 = "update b2b_vip_rights_ins set work_end_time = '%s', `gmt_modified` =now() where partner_name='%s';";
    private static String sql3 = "update b2b_vip_grade_ins set effective_end_time = '%s',days=180, `gmt_modified` =now() where id =  (select grade_ins_id from  b2b_vip_management  where partner_name='%s');";

    // 开通记录订正
    private static String sql4 = "update b2b_vip_management_grant set manager_id= %s,manager_name='%s',dept_name='%s' ,`gmt_modified` =now()where partner_name='%s';";
    // 充值记录订正
    private static String sql5 = "update b2b_partner_balance_recharge set manager_id= %s,manager_name='%s',dept_name='%s' ,`gmt_modified` =now()where partner_name='%s';";

    // 订正会员有效期，开通时间
    private static String sql6 = "UPDATE b2b_vip_management set effective_start_time='%s',effective_end_time='%s',gmt_modified=now() where partner_id =%s ;";
    private static String sql7 = "UPDATE b2b_vip_grade_ins set effective_start_time='%s',effective_end_time='%s',gmt_modified=now() where id = (SELECT grade_ins_id from b2b_vip_management where partner_id=%s and is_deleted = 0 ) ;";
    private static String sql8 = "UPDATE b2b_vip_rights_ins set work_start_time = '%s', work_end_time='%s',gmt_modified=now() where partner_id =%s ;";

    // 会员开通记录退费、关联主体
    private static String sql9 = "UPDATE b2b_vip_management_grant  SET refund_operator='%s',refund_amount=%s,refund_time='%s',refund_remark='%s' WHERE vip_management_grant_no='%s';";
    private static String sql10 = "UPDATE b2b_vip_management_grant  SET associate_operator='%s',associate_time='%s',associate_partner_name='%s' WHERE vip_management_grant_no='%s';";

    // 订正业务经理
    private static String sql11 = "update b2b_vip_management_grant set manager_id =%s ,manager_name='%s',dept_name='%s' ,gmt_modified=now() where vip_management_grant_no = '%s';";

    // 订正支付截止时间
    private static String sql12 = "update b2b_vip_management_installment set deadline='%s',gmt_modified= now() where vip_management_grant_no='%s' and pay_batch = 2;";

    // 退会员数据订正
    private static String sql13 = "UPDATE b2b_vip_management_grant set refund_operator='西瓜', refund_time = '%s',refund_amount= %s ,gmt_modified = now() where vip_management_grant_no = '%s';";

    // 订正开通记录feeCode为空的数据
    private static String sql14 = "update b2b_vip_management_grant set fee_code='%s',gmt_modified = now() where vip_management_grant_no='%s' and fee_code = '' ;";

    // -- 订正会员有效期
    private static String sql15 ="update b2b_vip_management set effective_end_time='%s',gmt_modified=now() where partner_name = '%s';";
    // -- 订正等级实例有效期
    private static String sql16 ="update b2b_vip_grade_ins set effective_end_time='%s',gmt_modified=now() where vip_id = (select id from b2b_vip_management where partner_name = '%s' ) and status = 1;";
    // -- 订正“有效”的权益实例有效期 (0：预生成，1:待生效，2：未使用，3：生效中(无限次)，4：已使用，5：冻结中，6：已过期，7：已失效)
    private static String sql17 ="update b2b_vip_rights_ins set work_end_time = '%s',gmt_modified=now()  where partner_name = '%s' and status in (2,3) and is_deleted = 0;";

    // 订正退款时间
    private static String sql18 ="UPDATE b2b_vip_management_grant set refund_time = '%s',gmt_modified = now() where vip_management_grant_no = '%s';";


    @Test
    public void contractCorrect14(){
        ExcelReader reader = ExcelUtil.getReader("/Users/mac/Desktop/2021年12月会员数据订正/20212115_会员退款订正.xlsx");

        List<RefundVipVO> vos = reader.readAll(RefundVipVO.class);
        FileWriter fileWriter = new FileWriter("updateSql.sql");
        fileWriter.append("-- 订正退款时间\n");
        for (RefundVipVO vo : vos) {
            String format = String.format(sql18,vo.getRefundTime(),vo.getGrantNO() );
            fileWriter.append(format+"\n");
        }
    }


    @Test
    public void contractCorrect13(){
        ExcelReader reader = ExcelUtil.getReader("/Users/mac/Desktop/2021年12月会员数据订正/20211231_分销通会员延长有效期.xlsx");

        List<VipVO1> vos = reader.readAll(VipVO1.class);
        FileWriter fileWriter = new FileWriter("updateSql.sql");
        fileWriter.append("-- 延长分销通会员有效期\n");
        for (VipVO1 vo : vos) {
            String format = String.format(sql15,vo.getEndTime(),vo.getPartnerName() );
            fileWriter.append("-- "+vo.getPartnerName()+"\n");
            fileWriter.append(format+"\n");
            format = String.format(sql16,vo.getEndTime(),vo.getPartnerName() );
            fileWriter.append(format+"\n");
            format = String.format(sql17,vo.getEndTime(),vo.getPartnerName() );
            fileWriter.append(format+"\n");

        }
    }

    @Test
    public void contractCorrect12(){
        ExcelReader reader = ExcelUtil.getReader("/Users/mac/Desktop/2021年11月会员数据订正/工作簿3.xlsx");

        List<VipGrantVO2> vos = reader.readAll(VipGrantVO2.class);
        FileWriter fileWriter = new FileWriter("updateSql.sql");
        fileWriter.append("-- 订正开通记录feeCode为空的数据\n");
        for (VipGrantVO2 vo : vos) {
            String format = String.format(sql14, vo.getFeeCode(),vo.getGrantNO());
            fileWriter.append(format+"\n");
        }
    }

    @Test
    public void contractCorrect11(){
        ExcelReader reader = ExcelUtil.getReader("/Users/mac/Desktop/2021年11月会员数据订正/20211119_退费订正.xlsx");

        List<RefundVipVO> vos = reader.readAll(RefundVipVO.class);
        FileWriter fileWriter = new FileWriter("updateSql.sql");
        fileWriter.append("-- 退会员数据订正\n");
        for (RefundVipVO vo : vos) {
            String format = String.format(sql13, vo.getRefundTime(),vo.getRefundAmount(),vo.getGrantNO());
            fileWriter.append(format+"\n");
        }
    }

    @Test
    public void contractCorrect10(){
        ExcelReader reader = ExcelUtil.getReader("/Users/mac/Desktop/2021年11月会员数据订正/20211112_中大b叠加购买会员订正截止时间.xlsx");

        List<DeadLineVO> vos = reader.readAll(DeadLineVO.class);
        FileWriter fileWriter = new FileWriter("updateSql.sql");
        fileWriter.append("-- 订正支付截止时间\n");
        for (DeadLineVO vo : vos) {
            String format = String.format(sql12, vo.getDeadline(),vo.getGrantNo());
            fileWriter.append(format+"\n");
        }
    }

    @Test
    public void contractCorrect9(){
        ExcelReader reader = ExcelUtil.getReader("/Users/mac/Desktop/小程序会员导入/订正业务经理2021103102.xlsx");
        List<VipGrantManagerVO> vos = reader.readAll(VipGrantManagerVO.class);
        FileWriter fileWriter = new FileWriter("updateSql.sql");
        fileWriter.append("-- 开通记录订正业务经理\n");
        for (VipGrantManagerVO vo : vos) {
            String format = String.format(sql11, vo.getManagerId(),vo.getManagerName(),vo.getDetpName(),vo.getGrantNO());
            fileWriter.append(format+"\n");
        }
    }

    @Test
    public void contractCorrect8(){
        ExcelReader reader = ExcelUtil.getReader("/Users/mac/Desktop/小程序会员导入/会员开通记录退费关联主体.xlsx");
        List<VipGrantVO> vos = reader.readAll(VipGrantVO.class);
        FileWriter fileWriter = new FileWriter("updateSql.sql");
        fileWriter.append("-- 订正开通记录退费、关联主体\n");
        for (VipGrantVO vo : vos) {
            if(StringUtils.isNotBlank(vo.getRefundOperator())){

                String format = String.format(sql9, vo.getRefundOperator(),vo.getRefund_amount(),vo.getRefund_time(),vo.getRefund_remark(),vo.getGrantNO());
                fileWriter.append(format+"\n");
            }else {
                String format = String.format(sql10, vo.getAssociate_operator(),vo.getAssociate_time(),vo.getAssociate_partner_name(),vo.getGrantNO());
                fileWriter.append(format+"\n");
            }
        }
    }

    @Test
    public void contractCorrect7(){
        ExcelReader reader = ExcelUtil.getReader("/Users/mac/Desktop/小程序会员导入/工作簿9.xlsx");
        List<VipInfoVO> vos = reader.readAll(VipInfoVO.class);
        FileWriter fileWriter = new FileWriter("updateSql.sql");
        fileWriter.append("-- 订正会员有效期\n");
        for (VipInfoVO vo : vos) {
            fileWriter.append("-- "+vo.getPartnerName()+"\n");

            String format = String.format(sql6, vo.getStartTime(),vo.getEndTime(),vo.getPartnerId());
            fileWriter.append(format+"\n");
            String format2 = String.format(sql7, vo.getStartTime(),vo.getEndTime(),vo.getPartnerId());
            fileWriter.append(format2+"\n");
            String format3 = String.format(sql8, vo.getStartTime(),vo.getEndTime(),vo.getPartnerId());
            fileWriter.append(format3+"\n");
        }
    }


    /**
     * 老会员合同数据订正
     *
     */
    // GMYTODO: 2021/6/8 10:11 上午 这个会修改“会员开通时间”，注意那些开通时间 > 生效时间的数据，需订正成开通时间=生效时间。
    @Test
    public void contractCorrect(){
        //ExcelReader reader = ExcelUtil.getReader("/Users/mac/Desktop/合同sql脚本.xlsx");
        ExcelReader reader = ExcelUtil.getReader("/Users/mac/Desktop/老会员合同订正/老会员合同订正20210730.xlsx");
        List<ContractVO> contractVOS = reader.readAll(ContractVO.class);
        FileWriter fileWriter = new FileWriter("updateSql.sql");
        fileWriter.append("# 签章表sql\n");
        for (ContractVO contractVO : contractVOS) {
            String format = String.format(signSQL, contractVO.getContractId(), contractVO.getContractNo(), contractVO.getContractDate(), contractVO.getOldContractId());
            fileWriter.append(format+"\n");
            System.out.println(format);
        }

        fileWriter.append("# 开通记录sql\n");
        for (ContractVO contractVO : contractVOS) {
            String format = String.format(grantSQL, contractVO.getContractId(),  contractVO.getContractDate(), contractVO.getOldContractId());
            fileWriter.append(format+"\n");
            System.out.println(format);
        }

        fileWriter.append("# 会员表sql\n");
        for (ContractVO contractVO : contractVOS) {
            String format = String.format(vipSQL, contractVO.getContractDate(), contractVO.getPartnerName());
            fileWriter.append(format+"\n");
            System.out.println(format);
        }
    }

    // GMYTODO: 2021/5/7 5:48 下午 应该用不到这个了，用合同任务id 更新成真正的合同id
    /**
     * 用合同任务id 更新成真正的合同id
     */
   /* @Test
    public void contractCorrect2(){
        ExcelReader reader = ExcelUtil.getReader("/Users/mac/Desktop/2021-04-30-15-50-08_EXPORT_XLSX_2526477_503_0.xlsx/2021-04-30-15-50-08_EXPORT_XLSX_2526477_503_0.xlsx");
        List<ContractIdVO> contractIdVOS = reader.readAll(ContractIdVO.class);
        FileWriter fileWriter = new FileWriter("updateSql.sql");

        fileWriter.append("# 签章表订正合同id sql\n");
        for (ContractIdVO contractIdVO : contractIdVOS) {
            String format = String.format(signSQLConstractId, contractIdVO.getContractId(),  contractIdVO.getTaskId());
            fileWriter.append(format+"\n");
            System.out.println(format);
        }

        fileWriter.append("# 开通记录订正合同id sql\n");
        for (ContractIdVO contractIdVO : contractIdVOS) {
            String format = String.format(grantSQLConstractId, contractIdVO.getContractId(),  contractIdVO.getTaskId());
            fileWriter.append(format+"\n");
            System.out.println(format);
        }
    }*/

    /**
     * 修改老会员开通时间
     */
    @Test
    public void contractCorrect3(){
        ExcelReader reader = ExcelUtil.getReader("/Users/mac/Desktop/中大B会员导入/中大B储值版开通时间.xlsx");
        List<ContractVO> contractVOS = reader.readAll(ContractVO.class);
        FileWriter fileWriter = new FileWriter("updateSql.sql");
        fileWriter.append("# 订正老会员开通时间\n");
        for (ContractVO contractVO : contractVOS) {
            String format = String.format(vipOpenTime, contractVO.getContractDate(),  contractVO.getPartnerName());
            fileWriter.append(format+"\n");
            System.out.println(format);
        }
    }

    @Test
    public void contractCorrect4(){
        ExcelReader reader = ExcelUtil.getReader("/Users/mac/Desktop/中大B会员导入/订正半年期.xlsx");
        List<VO> vos = reader.readAll(VO.class);
        FileWriter fileWriter = new FileWriter("updateSql.sql");
        fileWriter.append("# 订正会员信息\n");
        for (VO contractVO  : vos) {
            String format = String.format(sql1,   toDate(contractVO.getDate()),contractVO.getPartnerName());
            fileWriter.append(format+"\n");
            System.out.println(format);
        }
        fileWriter.append("# 订正权益信息\n");
        for (VO contractVO  : vos) {
            String format = String.format(sql2,   toDate(contractVO.getDate()),contractVO.getPartnerName());
            fileWriter.append(format+"\n");
            System.out.println(format);
        }
        fileWriter.append("# 订正等级实例\n");
        for (VO contractVO  : vos) {
            String format = String.format(sql3,   toDate(contractVO.getDate()),contractVO.getPartnerName());
            fileWriter.append(format+"\n");
            System.out.println(format);
        }
    }

    /**
     * 开通记录业务经理订正
     */
    @Test
    public void contractCorrect5(){
        ExcelReader reader = ExcelUtil.getReader("/Users/mac/Desktop/业务经理订正/工作簿2.xlsx");
        List<ManagerCorrectVO> vos = reader.readAll(ManagerCorrectVO.class);
        FileWriter fileWriter = new FileWriter("updateSql.sql");
        fileWriter.append("-- 订正开通记录业务经理\n");
        for (ManagerCorrectVO vo : vos) {
            String name = vo.getName();
            if(StringUtils.isNotBlank(vo.getNickName())){
                name = vo.getName()+"（"+vo.getNickName()+"）";
            }
            String format = String.format(sql4, vo.getId(),name,vo.getDeptName(),vo.getCorpName());
            fileWriter.append(format+"\n");
            System.out.println(format);
        }
    }

    /**
     * 充值记录订正
     */
    @Test
    public void contractCorrect6(){
        ExcelReader reader = ExcelUtil.getReader("/Users/mac/Desktop/业务经理订正/工作簿3.xlsx");
        List<ManagerCorrectVO> vos = reader.readAll(ManagerCorrectVO.class);
        FileWriter fileWriter = new FileWriter("updateSql.sql");
        fileWriter.append("-- 订正充值记录\n");
        for (ManagerCorrectVO vo : vos) {
            String name = vo.getName();
            if(StringUtils.isNotBlank(vo.getNickName())){
                name = vo.getName()+"（"+vo.getNickName()+"）";
            }
            String format = String.format(sql5, vo.getId(),name,vo.getDeptName(),vo.getCorpName());
            fileWriter.append(format+"\n");
            System.out.println(format);
        }
    }



    private String toDate(String date){
        DateTime dateTime = DateUtil.parseDate(date);
        return dateTime.toString(DatePattern.NORM_DATETIME_PATTERN);
    }
}
