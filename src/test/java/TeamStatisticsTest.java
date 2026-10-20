import api.GradeDataBase;
import entity.Grade;
import entity.Team;
import org.junit.jupiter.api.Test;
import usecase.GetAverageGradeUseCase;
import usecase.GetTopGradeUseCase;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TeamStatisticsTest {
    @Test void excludesOtherCoursesAndMembersWithoutTheCourse() {
        GradeDataBase db = mock(GradeDataBase.class);
        when(db.getMyTeam()).thenReturn(new Team("test", new String[]{"a", "b", "c"}));
        when(db.getGrades("a")).thenReturn(new Grade[]{new Grade("a", "207", 80), new Grade("a", "148", 100)});
        when(db.getGrades("b")).thenReturn(new Grade[]{new Grade("b", "207", 85)});
        when(db.getGrades("c")).thenReturn(new Grade[0]);
        assertEquals(82.5f, new GetAverageGradeUseCase(db).getAverageGrade("207"));
        assertEquals(85f, new GetTopGradeUseCase(db).getTopGrade("207"));
    }
    @Test void emptyTeamReturnsZero() {
        GradeDataBase db = mock(GradeDataBase.class);
        when(db.getMyTeam()).thenReturn(new Team("empty", new String[0]));
        assertEquals(0f, new GetAverageGradeUseCase(db).getAverageGrade("207"));
        assertEquals(0f, new GetTopGradeUseCase(db).getTopGrade("207"));
    }
    @Test void noMatchingCourseReturnsZero() {
        GradeDataBase db = mock(GradeDataBase.class);
        when(db.getMyTeam()).thenReturn(new Team("test", new String[]{"a"}));
        when(db.getGrades("a")).thenReturn(new Grade[]{new Grade("a", "148", 100)});
        assertEquals(0f, new GetAverageGradeUseCase(db).getAverageGrade("207"));
        assertEquals(0f, new GetTopGradeUseCase(db).getTopGrade("207"));
    }
}
