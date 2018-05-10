package com.innext.szqb.ui.my.bean

/**
 * @author      : yan
 * @date        : 2017/12/21 16:10
 * @description : 版本更新返回的数据bean
 */
data class VersionUpdateBean(var item: Item?)

data class Item(
		var packageUrl: String?, //http://www.baidu.com
		var description: String?, //abc
		var isUpdate: String? //1
)