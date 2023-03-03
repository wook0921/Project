package second.sosu.common.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

public class AbstractDAO {
   protected Log log = LogFactory.getLog(AbstractDAO.class);
   
   @Autowired//컨테이너에서 해당 [타입]의 객체를 찾아 주입
   private SqlSessionTemplate sqlSession;
   
   protected void pringQueryId(String queryId) {//로그용
      if(log.isDebugEnabled()) log.debug("\t QueryId \t "+queryId);
   }
   public Object insert(String queryId, Object params) {//삽입
      pringQueryId(queryId);
      return sqlSession.insert(queryId, params);
   }
   public Object update(String queryId, Object params) {//수정
      pringQueryId(queryId);
      return sqlSession.update(queryId, params);
   }
   public Object update2(String queryId) {//모임 자동마감
         pringQueryId(queryId);
         return sqlSession.update(queryId);
   }
   public Object delete(String queryId, Object params) {//삭제
      pringQueryId(queryId);
      return sqlSession.delete(queryId, params);
   }
   public Object selectOne(String queryId) {//한줄
      pringQueryId(queryId);
      return sqlSession.selectOne(queryId);
   }
   public Object selectOne(String queryId, Object params) {
      pringQueryId(queryId);
      return sqlSession.selectOne(queryId, params);
   }
   @SuppressWarnings("rawtypes")
   public List selectList(String queryId) {//여러줄
      pringQueryId(queryId);
      return sqlSession.selectList(queryId);
   }
   @SuppressWarnings("rawtypes")
   public List selectList(String queryId, Object params) {
      pringQueryId(queryId);
      return sqlSession.selectList(queryId, params);
   }
   @SuppressWarnings({"rawtypes", "unchecked"})
   public Map selectPagingList(String queryId, Object params) {
      //전달받은 Object를 Map 타입으로 변환
      Map<String, Object> map = (Map<String, Object>)params;
      //리턴용 Map
      Map<String, Object> returnMap = new HashMap<String, Object>();
      pringQueryId(queryId);//로그 찍기
      
      //전자정부 프레임워크에서 제공하는 페이징 객체 생성
      PaginationInfo paginationInfo = new PaginationInfo();
      //현재 페이지 번호 세팅. 값이 없을 경우 1로 초기화
      if(!map.containsKey("currentPageNo") || StringUtils.isEmpty(map.get("currentPageNo")))
         map.put("currentPageNo", "1");
      paginationInfo.setCurrentPageNo(Integer.parseInt(map.get("currentPageNo").toString()));
      //한 페이지당 출력 게시물 개수 세팅. 값이 없을 경우 15로 초기화
      if(!map.containsKey("PAGE_ROW") || StringUtils.isEmpty(map.get("PAGE_ROW")))
         map.put("PAGE_ROW", "15");
      paginationInfo.setRecordCountPerPage(Integer.parseInt(map.get("PAGE_ROW").toString()));
      //한 페이징에 출력되는 페이징 수 세팅
      paginationInfo.setPageSize(10);
      //페이징 SQL에 사용되는 rownum 값 세팅
      int start = paginationInfo.getFirstRecordIndex();//시작 게시물 번호
      int end = start + paginationInfo.getRecordCountPerPage();//종료 게시물 번호
      map.put("START", start+1);
      map.put("END", end);
      
      params = map;
      //쿼리문 실행
      List<Map<String, Object>> list = sqlSession.selectList(queryId, params);
      //실행 결과
      if(list.size() == 0) {//리턴된 게시글이 없으면
         map = new HashMap<String, Object>();
         map.put("TOTAL_COUNT", 0);//총 게시글 개수 0으로 세팅
         list.add(map);//list에 추가
         if(paginationInfo != null) {//페이징 값이 있다면
            paginationInfo.setTotalRecordCount(0);
            returnMap.put("paginationInfo", paginationInfo);
         }
      }else {
         if(paginationInfo != null) {
            paginationInfo.setTotalRecordCount(Integer.parseInt(
                  list.get(0).get("TOTAL_COUNT").toString()));
            returnMap.put("paginationInfo", paginationInfo);
         }
      }
      returnMap.put("result", list);
      return returnMap;
   }
   @SuppressWarnings("unchecked")
   public Object selectPagingJson(String queryId, Object params) {
      //전달받은 Object를 Map 타입으로 변환
      Map<String, Object> map = (Map<String, Object>)params;
      pringQueryId(queryId);//로그 찍기
       
      //현재 페이지. 값이 없으면 1
      int currentPageNo = StringUtils.isEmpty(map.get("CURRENT_PAGE_NO"))?1:Integer.parseInt(map.get("CURRENT_PAGE_NO").toString());
      //한 페이지당 출력 게시물 개수. 값이 없으면 15
      int page_row = StringUtils.isEmpty(map.get("PAGE_ROW"))?15:Integer.parseInt(map.get("PAGE_ROW").toString());
      
      //페이징 SQL에 사용되는 rownum 값 세팅
      int start = (currentPageNo-1) * page_row;//시작 게시물 번호
      int end = start + page_row;//종료 게시물 번호
      map.put("START", start+1);
      map.put("END", end);
      
      return sqlSession.selectList(queryId, map);
   }
}