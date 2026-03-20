package org.epst.online;

import java.util.Set;

public final class OnlineRoleMapper {
    private static final Set<Integer> INSPECTOR_ROLES = Set.of(
            7, 8, 9, 10, 13, 14, 15, 16, 19, 20
    );

    private static final Set<Integer> ADMIN_ROLES = Set.of(
            0, 17, 18
    );

    private OnlineRoleMapper() {
    }

    public static boolean isInspectorRole(int role) {
        return INSPECTOR_ROLES.contains(role);
    }

    public static boolean isAdminRole(int role) {
        return ADMIN_ROLES.contains(role);
    }
}

