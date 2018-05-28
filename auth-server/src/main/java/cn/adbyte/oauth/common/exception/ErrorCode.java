package cn.adbyte.oauth.common.exception;

/**
 * Created by Adam.yao on 2017/10/25.
 */
public enum ErrorCode {

    /************************************
     * 统一异常捕获（50***）错误码
     ***********************************/
    INTERNAL_PROGRAM_ERROR(50000, "程序内部错误"),
    DataAccessException(50001, "数据操作异常"),
    ConstraintViolationException(50002, "违反数据库唯一约束异常"),
    DataIntegrityViolationException(50003, "违反数据库唯一约束异常"),
    MySQLIntegrityConstraintViolationException(50004, "违反数据库唯一约束异常"),
    NullPointerException(50005, "空指针异常"),
    IOException(50006, "IO异常"),
    ClassNotFoundException(50007, "指定的类不存在"),
    ArithmeticException(50008, "数学运算异常"),
    ArrayIndexOutOfBoundsException(50009, "数组下标越界"),
    IllegalArgumentException(50010, "参数错误或非法"),
    ClassCastException(50011, "类型强制转换错误"),
    SQLException(50013, "操作数据库异常"),
    SecurityException(50012, "违背安全原则异常"),
    NoSuchMethodError(50014, "方法末找到异常"),
    InternalError(50015, "虚拟机发生了内部错误"),
    ConnectException(50016, "服务器连接异常"),
    CancellationException(50017, "任务已被取消的异常"),
    ApiException(50018, "阿里服务器错误"),
    ParseException(50019, "日期格式错误"),
    ReturnSizeTooLargeException(50020, "返回尺寸过大错误"),
    EntityNotFoundException(50021, "实体对象未找到异常"),
    RealDelException(50022, "调用异常, 不可删除或未使用标记删除"),
    InvalidDataAccessApiUsageException(50023, "无效的数据访问Api使用异常"),
    JSONException(50024, "JSON格式数据转换错误"),


    /************************************
     * 参数异常（51***）错误码
     ***********************************/
    ParaIsNull(51002, "参数为空"),
    paraNotRight(51003, "参数非法"),
    UsernameNotFoundException(51004, "用户不存在"),
    BadCredentialsException(51005, "登陆凭证错误"),
    DisabledException(51006, "用户不可用"),
    AuthenticationException(51007, "权限验证失败"),
    InternalAuthenticationServiceException(51008, "内部权限验证失败"),


    /************************************
     * 公共操作成功、失败（60***）错误码
     ***********************************/
    HANDLER_SUCCESS(60000, "操作成功"),
    HANDLER_FAILED(60001, "操作失败"),
    SAVE_SUCCESS(60002, "新增成功"),
    SAVE_FAILED(60003, "新增失败"),
    DELETE_SUCCESS(60004, "删除成功"),
    DELETE_FAILED(60005, "删除失败"),
    UPDATE_SUCCESS(60006, "修改成功"),
    UPDATE_FAILED(60007, "修改失败"),
    SET_SUCCESS(60008, "设置成功"),
    SET_FAILED(60009, "设置失败"),
    NO_DATA(60010, "无对应数据"),
    SYNC_SUCCESS(60011, "同步成功"),
    SYNC_FAILED(60012, "同步失败"),
    SYNC_DATA_IS_NULL(60013, "同步数据为空"),
    SYNC_DATA_NOT_ALL_SUCCESS(60014, "同步数据部分成功"),
    FIND_SUCCESS(60015, "查询成功"),
    FIND_FAILED(60016, "查询失败"),

    /************************************
     * 尋源操作成功、失败（70***）错误码
     ***********************************/
    DepartmentError(70001, "部门选择不正确"),
    GoodsNumError(70002, "商品数量不能小于或等于0"),
    NoRight(70003, "对不起,您没有权利操作"),
    SubmitSuccess(70004, "提交成功"),
    SubmitError(70005, "提交失败"),
    SaveSuccess(70006, "保存成功"),
    SaveError(70007, "保存失败"),
    RemoveSuccess(70008, "删除成功"),
    RemoveError(70009, "删除失败"),
    UpdateSuccess(70010, "修改成功"),
    UpdateError(70011, "修改失败"),
    GetError(70012, "未获取到信息"),
    GoodsNotFoundError(70013, "请添加商品"),
    CancelSuccess(70014, "取消成功"),
    CancelError(70015, "取消失败"),
    NonCancelAbilityError(70016, "该询已存在报价，不能结束或取消该询价"),
    QuoteFreightLimitError(70017, "运费输入不正确"),
    NullError(70018, "请输入必填项"),
    QuoteMinTimeStrategyError(70019, "报价最小时间不能小于30天"),
    WebError(70020, "页面异常,请重新操作"),
    ExpireError(70021, "询价截止日期不能小于等于当前时间"),
    PackageNumError(70022, "包装商品数量不能小于或等于0"),
    InquiryGoodsOverdueError(70023, "该询价商品结束"),
    RequiredError(70024,"必填非空验证"),
    QuotationGoodsAmountError(70025, "总价验证失败"),
    InquiryStatusError(70026, "询价单及状态验证失败"),
    InquiryVendorScopeError(70027, "询价供应商范围验证失败"),
    InquiryQuoteMinTimeStrategyError(70028, "报价有效期设置过短"),
    InquiryQuoteTotalSpecStrategyError(70029, "询价商品替代要求规格验证失败"),
    InquiryQuoteTotalStrategyError(70030, "询价商品替代要求验证失败"),
    SystemUnknownError(70031, "系统内部异常，请联系管理员"),
    CategoryNullError(70032, "请选择平台类目"),;

    public Number code;
    public String des;

    private ErrorCode(Number code, String des) {
        this.code = code;
        this.des = des;
    }

    public static ErrorCode get(Number code) {
        for (ErrorCode errorCode : ErrorCode.values()) {
            if (errorCode.code.toString().equals(code.toString())) {
                return errorCode;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "code:" + code + ", des:" + des;
    }

    public static void main(String[] args) {
        ErrorCode errorCode = get(60000);
        if (null != errorCode) {
            System.err.println(errorCode.code + "<==========>" + errorCode.des);
        }
        System.out.println(errorCode);
    }
}