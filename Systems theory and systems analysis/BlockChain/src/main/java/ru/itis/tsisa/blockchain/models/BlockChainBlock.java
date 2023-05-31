package ru.itis.tsisa.blockchain.models;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
@Entity
@Table(name = "blockchain_block")
public class BlockChainBlock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @Column(name = "miner_id", nullable = false)
    private int minerId;

    @Column(nullable = false)
    private long none;

    @Column(nullable = false)
    private byte[] hash;

    @Column(name = "previous_hash", nullable = false)
    private byte[] previousHash;

    @Column(name = "miner_content_sign", nullable = false)
    private byte[] minerContentSign;

    @Column(name = "miner_block_sign", nullable = false)
    private byte[] minerBlockSign;

    @Column(name = "auditor_content_sign", nullable = false)
    private byte[] auditorContentSign;

    @Column(name = "auditor_block_sign", nullable = false)
    private byte[] auditorBlockSign;

    @Column(name = "content_sign_time", nullable = false)
    private String contentSignTime;

    @Column(name = "block_sign_time", nullable = false)
    private String blockSignTime;

    @Column(name = "add_time", nullable = false)
    private LocalDateTime addTime;

}
