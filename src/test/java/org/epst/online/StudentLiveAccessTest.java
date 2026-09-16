package org.epst.online;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class StudentLiveAccessTest {
    @Test
    void studentCannotObtainRoomWithoutSchoolIdentifier() {
        LiveSessionResource resource = new LiveSessionResource();
        LiveSessionResource.TeacherAccessRequest request = new LiveSessionResource.TeacherAccessRequest();
        request.accessKey = "123456";
        request.classId = "1 Primaire";
        request.matricule = "  ";
        assertEquals(400, resource.studentAccess(request).getStatus());
    }

    @Test
    void classMatchingHonoursWordBoundariesAndAccents() {
        assertTrue(ClassLabelUtil.matchesLoose("1ère Primaire", "1ère Primaire A"));
        assertTrue(ClassLabelUtil.matchesLoose("1ère Primaire", "1ere primaire"));
        assertTrue(ClassLabelUtil.matchesLoose("1 Primaire", "1ère Primaire A"));
        assertTrue(ClassLabelUtil.matchesLoose("2ème secondaire", "2e Secondaire"));
        assertFalse(ClassLabelUtil.matchesLoose("1", "10 Primaire"));
        assertFalse(ClassLabelUtil.matchesLoose("1 Primaire", "2 Primaire"));
        assertFalse(ClassLabelUtil.matchesLoose("---", "1 Primaire"));
        assertFalse(ClassLabelUtil.matchesLoose("1 Primaire", null));
    }

    @Test
    void streamingInspectorHasDedicatedRole() {
        assertTrue(OnlineRoleMapper.isStreamingInspectorRole(21));
        assertTrue(OnlineRoleMapper.isInspectorRole(21));
        assertFalse(OnlineRoleMapper.isStreamingInspectorRole(20));
        assertFalse(OnlineRoleMapper.isAdminRole(21));
    }
}
