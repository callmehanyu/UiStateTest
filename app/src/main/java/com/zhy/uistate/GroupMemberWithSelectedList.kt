package com.zhy.uistate

import com.zhy.demo.GroupMemberWithSelected
import kotlin.collections.List

/**
 * com.zhy.demo.GroupMemberWithSelected_0
 */
public val groupMemberWithSelected_0: GroupMemberWithSelected = GroupMemberWithSelected(
    member = com.zhy.demo.GroupMember(
    uk = -1,
    name = "群成员1",
    avatarUrl = "https://c-ssl.duitang.com/uploads/blog/202106/13/20210613235426_7a793.jpeg",
    ),
    isSelected = true,
    )

/**
 * com.zhy.demo.GroupMemberWithSelected_1
 */
public val groupMemberWithSelected_1: GroupMemberWithSelected = GroupMemberWithSelected(
    member = com.zhy.demo.GroupMember(
    uk = -1,
    name = "群成员1",
    avatarUrl = "https://c-ssl.duitang.com/uploads/blog/202106/13/20210613235426_7a793.jpeg",
    ),
    isSelected = false,
    )

/**
 * com.zhy.demo.GroupMemberWithSelected_List
 */
public val groupMemberWithSelected_List: List<GroupMemberWithSelected> =
    listOf(groupMemberWithSelected_0, groupMemberWithSelected_1, )
