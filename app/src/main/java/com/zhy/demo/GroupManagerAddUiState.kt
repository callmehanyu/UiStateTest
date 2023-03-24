package com.zhy.demo

import android.os.Parcelable
import com.mock.annotation.UiStateTest
import com.mock.annotation.UiStateTestDeclared
import com.mock.annotation.custom.UiStateTestCustomInt
import com.mock.annotation.custom.UiStateTestCustomString
import com.mock.annotation.unique.UiStateTestUnique
import kotlinx.android.parcel.Parcelize

@UiStateTest
data class GroupManagerAddUiState(
//	@UiStateTestUnique
//	val memberList: List<GroupManagerAddViewHolderParam> = emptyList(),
//	@UiStateTestUnique
//	val selectedMemberEntry: SelectedMemberEntry = SelectedMemberEntry(),
	@UiStateTestUnique
	val isShowConfirm: Boolean = false,
	@UiStateTestUnique
	val selectedDialog: SelectedMembersDialog = SelectedMembersDialog(),
)

/**
 * 查看已选成员弹窗 UiState
 */
@UiStateTestDeclared
data class SelectedMembersDialog(
	@UiStateTestUnique
	val isShow: Boolean? = false,
	@UiStateTestUnique
	val memberList: List<GroupMemberWithSelected> = emptyList(),
)

/**
 * 移出群管理员列表 参数
 */
@UiStateTestDeclared
data class GroupMemberWithSelected(
	@UiStateTestUnique
	val member: GroupMember,
	@UiStateTestUnique
	val isSelected: Boolean,
)

/**
 * 群成员
 */
@UiStateTestDeclared
@Parcelize
data class GroupMember(
	@UiStateTestCustomInt(-1)
	val uk: Long,
	@UiStateTestCustomString("群成员1")
	val name: String,
	@UiStateTestCustomString("https://c-ssl.duitang.com/uploads/blog/202106/13/20210613235426_7a793.jpeg")
	val avatarUrl: String,
): Parcelable