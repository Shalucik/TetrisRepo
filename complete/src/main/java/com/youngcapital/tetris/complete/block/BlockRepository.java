package com.youngcapital.tetris.complete.block;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface BlockRepository extends CrudRepository<Block, Long> {
	List<Block> findByBlockType(String blockType);
}
