package tech.finovy.framework.result;

/**
 * 系统常量
 */
public class GlobalConstant {
	private GlobalConstant() {
	}

	/**
	 * 编码
	 */
	public static  final String UTF_8 = "UTF-8";

	/**
	 * contentType
	 */
	public static  final String CONTENT_TYPE_NAME = "Content-type";

	/**
	 * JSON 资源
	 */
	public static  final String CONTENT_TYPE = "application/json;charset=utf-8";

	/**
	 * 管理员对应的租户ID
	 */
	public static  final String ADMIN_TENANT_ID = "000000";

	/**
	 * 默认为空消息
	 */
	public static  final String DEFAULT_NULL_MESSAGE = "data null";
	/**
	 * 默认成功消息
	 */
	public static  final String DEFAULT_SUCCESS_MESSAGE = "success";
	/**
	 * 默认失败消息
	 */
	public static  final String DEFAULT_FAILURE_MESSAGE = "failure";
	/**
	 * 默认未授权消息
	 */
	public static  final String DEFAULT_UNAUTHORIZED_MESSAGE = "unauthorized";

}
