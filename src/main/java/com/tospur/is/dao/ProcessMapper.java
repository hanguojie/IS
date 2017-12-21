package com.tospur.is.dao;

import java.util.List;

import com.tospur.is.vo.RunforVo;
import com.tospur.is.vo.Vote;

public interface ProcessMapper {

	public int addrunFor(RunforVo runforVo);
	
	public List<RunforVo> getPInfoByType(String type);

	public List<RunforVo> findPListByType(String type);

	public List<RunforVo> findAddList();
	
	public List<RunforVo> getByWorkNo(String workNo);
	
	public int addVote(Vote vote);
	
	public List<Vote> findVoteNum(Vote vote);

	public int updateStatus(RunforVo runforVo);

	public List<RunforVo> findRankingByType(String type);

	public List<RunforVo> findAllList();
}

