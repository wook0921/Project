package second.sosu.moim.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import second.sosu.common.domain.CommandMap;
import second.sosu.common.util.FileUtils;
import second.sosu.moim.dao.MoimDao;

@Service("moimService")
public class MoimServiceImpl implements MoimService {

	Logger log = Logger.getLogger(this.getClass());

	@Resource(name = "moimDao")
	private MoimDao moimDao;

	@Resource(name = "fileUtils")
	private FileUtils fileUtils;

	// 모임 리스트
	@Override
	public List<Map<String, Object>> moimList(Map<String, Object> map, HttpSession session) throws Exception {

		return moimDao.moimList(map, session);
	}

	// 모임 검색 리스트
	@Override
	public List<Map<String, Object>> moimSearchList(Map<String, Object> map, HttpSession session, CommandMap commandMap)
			throws Exception {

		return moimDao.moimSearchList(map, session, commandMap);
	}

	// 모임 갯수
	@Override
	public Map<String, Object> moimCount(Map<String, Object> map) throws Exception {
		Map<String, Object> resultMap = moimDao.moimCount(map);

		return resultMap;
	}

	// 페이징을 위한 모임 토탈 카운트
	@Override
	public int moimTotalCount(Map<String, Object> map) {
		return moimDao.moimTotalCount(map);
	}

	// 모임 상세보기
	@Override
	public Map<String, Object> moimDetail(Map<String, Object> map, HttpSession session) throws Exception {
		Map<String, Object> resultMap = moimDao.moimDetail(map, session);

		return resultMap;
	}

	// 모임 상세 이미지
	@Override
	public List<Map<String, Object>> selectMoimImg(Map<String, Object> map, CommandMap commandMap) throws Exception {
		return moimDao.selectMoimImg(map, commandMap);
	}

	// 모임에 참가한 인원 리스트
	@Override
	public List<Map<String, Object>> moimMemberList(Map<String, Object> map, CommandMap commandMap) throws Exception {
		// TODO Auto-generated method stub
		return moimDao.moimMemberList(map, commandMap);
	}

	// 모임참여에 대기중인 인원 리스트
	@Override
	public List<Map<String, Object>> moimMemberWait(Map<String, Object> map, CommandMap commandMap) throws Exception {
		// TODO Auto-generated method stub
		return moimDao.moimMemberWait(map, commandMap);
	}

	// 모임 작성
	@Override
	public void moimRegister(Map<String, Object> map, HttpSession session, HttpServletRequest request)
			throws Exception {

		moimDao.moimRegister(map, session);

		List<Map<String, Object>> list = fileUtils.fileInsert(map, request);

		for (int i = 0, size = list.size(); i < size; i++) {
			moimDao.moimImg(list.get(i));
		}
	}

	// 모임 참가
	@Override
	public void moimJoin(Map<String, Object> map, HttpSession session, CommandMap commandMap) throws Exception {
		// TODO Auto-generated method stub

		moimDao.moimJoin(map, session, commandMap);

	}

	// 모임 참가
	@Override
	public void moimReJoin(Map<String, Object> map, HttpSession session, CommandMap commandMap) throws Exception {
		// TODO Auto-generated method stub

		moimDao.moimReJoin(map, session, commandMap);

	}

	// 모임 재참여 대기
	@Override
	public void moimRePermit(Map<String, Object> map, HttpSession session, CommandMap commandMap) throws Exception {
		// TODO Auto-generated method stub

		moimDao.moimRePermit(map, session, commandMap);

	}

	// 모임 수정
	@Override
	public void moimModify(Map<String, Object> map, HttpServletRequest request) throws Exception {

		moimDao.moimModify(map);

		List<Map<String, Object>> list = fileUtils.fileInsert(map, request);

		for (int i = 0, size = list.size(); i < size; i++) {
			moimDao.moimImg(list.get(i));
		}
	}

	// 모임 이미지 삭제
	@Override
	public void MoimImgDelete(String F_SVNAME) throws Exception {

		moimDao.MoimImgDelete(F_SVNAME);
	}

	// 모임 참가 (승인이 필요한 경우)
	@Override
	public void moimJoinPermit(Map<String, Object> map, HttpSession session, CommandMap commandMap) throws Exception {
		// TODO Auto-generated method stub
		moimDao.moimJoinPermit(map, session, commandMap);

	}

	// 모임 참여승인
	@Override
	public void moimWaitApprove(Map<String, Object> map) throws Exception {
		moimDao.moimWaitApprove(map);
	}

	// 모임 탈퇴하기
	@Override
	public void moimExit(Map<String, Object> map, HttpSession session) throws Exception {
		moimDao.moimExit(map);
	}

	// 모임 맴버 강퇴
	@Override
	public void moimMemberBan(Map<String, Object> map) throws Exception {
		moimDao.moimMemberBan(map);
	}

	// 모임 삭제
	@Override
	public void moimDelete(Map<String, Object> map) throws Exception {
		moimDao.moimDelete(map);
	}

	// 모임 조기 마감
	@Override
	public void moimSelfClose(Map<String, Object> map) throws Exception {
		moimDao.moimSelfClose(map);
	}

	// 모임 자동 마감
	@Override
	public void moimClose() throws Exception {
		moimDao.moimClose();
	}

}