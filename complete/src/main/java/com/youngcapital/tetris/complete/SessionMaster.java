package com.youngcapital.tetris.complete;

import java.util.LinkedList;

import javax.servlet.http.HttpSession;

import com.youngcapital.tetris.complete.block.TetrisBlock;

public class SessionMaster {
	
	private HttpSession session;
	private TetrisMaster tetrisMaster;
	private ModelMaster modelMaster;
	private TetrisBlock currentBlock;
	private LinkedList<TetrisBlock> currentBlockQueue;

	public SessionMaster(HttpSession session, TetrisMaster tetrisMaster, ModelMaster modelMaster,
			TetrisBlock currentBlock) {
		this.session = session;
		this.tetrisMaster = tetrisMaster;
		this.modelMaster = modelMaster;
		this.currentBlock = currentBlock;
		currentBlockQueue = new LinkedList<TetrisBlock>();
		currentBlockQueue.addLast(modelMaster.createBlock());
	}

	public HttpSession getSession() {
		return session;
	}

	public void setSession(HttpSession session) {
		this.session = session;
	}

	public TetrisMaster getTetrisMaster() {
		return tetrisMaster;
	}

	public void setTetrisMaster(TetrisMaster tetrisMaster) {
		this.tetrisMaster = tetrisMaster;
	}

	public ModelMaster getModelMaster() {
		return modelMaster;
	}

	public void setModelMaster(ModelMaster modelMaster) {
		this.modelMaster = modelMaster;
	}

	public TetrisBlock getCurrentBlock() {
		return currentBlock;
	}

	public void setCurrentBlock(TetrisBlock currentBlock) {
		this.currentBlock = currentBlock;
	}
	
	public LinkedList<TetrisBlock> getCurrentBlockQueue() {
		return currentBlockQueue;
	}

	public void setCurrentBlockQueue(LinkedList<TetrisBlock> currentBlockQueue) {
		this.currentBlockQueue = currentBlockQueue;
	}
}
