package com.zhy.uistate.app

import com.zhy.demo.GroupManagerAddUiState
import kotlin.collections.List

/**
 * com.zhy.demo.GroupManagerAddUiState_0
 */
public val groupManagerAddUiState_0: GroupManagerAddUiState = GroupManagerAddUiState(
    isShowConfirm = true,
    selectedDialog = com.zhy.demo.SelectedMembersDialog(
    isShow = true,
    memberList = groupMemberWithSelected_List,
    ),
    )

/**
 * com.zhy.demo.GroupManagerAddUiState_1
 */
public val groupManagerAddUiState_1: GroupManagerAddUiState = GroupManagerAddUiState(
    isShowConfirm = true,
    selectedDialog = com.zhy.demo.SelectedMembersDialog(
    isShow = false,
    memberList = groupMemberWithSelected_List,
    ),
    )

/**
 * com.zhy.demo.GroupManagerAddUiState_2
 */
public val groupManagerAddUiState_2: GroupManagerAddUiState = GroupManagerAddUiState(
    isShowConfirm = false,
    selectedDialog = com.zhy.demo.SelectedMembersDialog(
    isShow = true,
    memberList = groupMemberWithSelected_List,
    ),
    )

/**
 * com.zhy.demo.GroupManagerAddUiState_3
 */
public val groupManagerAddUiState_3: GroupManagerAddUiState = GroupManagerAddUiState(
    isShowConfirm = false,
    selectedDialog = com.zhy.demo.SelectedMembersDialog(
    isShow = false,
    memberList = groupMemberWithSelected_List,
    ),
    )

/**
 * com.zhy.demo.GroupManagerAddUiState_List
 */
public val groupManagerAddUiState_List: List<GroupManagerAddUiState> =
    listOf(groupManagerAddUiState_0, groupManagerAddUiState_1, groupManagerAddUiState_2,
    groupManagerAddUiState_3, )
