package pairmatching.service;

import camp.nextstep.edu.missionutils.Randoms;
import pairmatching.domain.*;
import pairmatching.repository.CrewRepository;
import pairmatching.repository.MatchInfoRepository;

import java.util.ArrayList;
import java.util.List;

import static pairmatching.utils.ExceptionMessage.*;
import static pairmatching.utils.Validate.checkEachInfo;

public class MatchingService {

    private static final MatchInfoRepository matchInfoRepository = MatchInfoRepository.getInstance();
    private static final CrewRepository crewRepository = CrewRepository.getInstance();

    public MatchInfo matchPair(String input, Missions missions) {
        String[] infoArr = input.split(", ");
        Course course = checkCourse(infoArr[0]);
        Level level = checkLevel(infoArr[1]);
        Mission mission = checkMission(infoArr[1], infoArr[2], missions);

        return matchAndSavePair(course, level, mission);
    }

    private Course checkCourse(String course) {
        checkEachInfo(course);
        if (!Course.isExist(course)) {
            throw new IllegalArgumentException(ERROR_COURSE_NOT_EXIST);
        }
        return Course.getEnumCourse(course);
    }

    private Level checkLevel(String level) {
        checkEachInfo(level);
        if (!Level.isExist(level)) {
            throw new IllegalArgumentException(ERROR_LEVEL_NOT_EXIST);
        }
        return Level.getEnumLevel(level);
    }

    private Mission checkMission(String level, String mission, Missions missions) {
        checkEachInfo(mission);
        if (!missions.isExist(level, mission)) {
            throw new IllegalArgumentException(ERROR_MISSION_NOT_EXIST);
        }
        return Mission.getEnumMission(mission);
    }

    public MatchInfo matchAndSavePair(Course course, Level level, Mission mission) {
        List<List<Crew>> pair = new ArrayList<>();
        List<Crew> crews = Randoms.shuffle(crewRepository.getCrew(course));
        int index = 0;
        while (crews.size() != index) {
            List<Crew> onePair = new ArrayList<>();
            onePair.add(crews.get(index++));
            onePair.add(crews.get(index++));
            if (index == crews.size() - 1) {
                onePair.add(crews.get(index++));
            }
            pair.add(onePair);
        }
        return matchInfoRepository.addMatchInfo(new MatchInfo(course, level, mission, pair));
    }

}