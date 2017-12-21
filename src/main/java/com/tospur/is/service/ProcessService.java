package com.tospur.is.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.tospur.is.dao.ProcessMapper;
import com.tospur.is.vo.RunforVo;
import com.tospur.is.vo.Vote;

@Service
public class ProcessService {
	@Resource
	private ProcessMapper processMapper;
	
	public int addrunFor(RunforVo runforVo){
		return processMapper.addrunFor(runforVo);
	}
	
	public List<RunforVo> getPInfoByType(String type){
		return processMapper.getPInfoByType(type);
	}

	public List<RunforVo> findPListByType(String type) {
		return processMapper.findPListByType(type);
	}

	public List<RunforVo> findAddList() {
		return processMapper.findAddList();
	}
	
	public List<RunforVo> getByWorkNo(String workNo) {
		return processMapper.getByWorkNo(workNo);
	}
	
	public int addVote(Vote vote){
		return processMapper.addVote(vote);
	}
	
	public List<Vote> findVoteNum(Vote vote) {
		return processMapper.findVoteNum(vote);
	}

	public int updateStatus(RunforVo runforVo) {
		return processMapper.updateStatus(runforVo);
		
	}

	public List<RunforVo> findRankingByType(String type) {
		return processMapper.findRankingByType(type);
	}

	public List<RunforVo> findAllList() {
		return processMapper.findAllList();
	}
}
