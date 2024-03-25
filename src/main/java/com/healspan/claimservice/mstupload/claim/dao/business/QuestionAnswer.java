package com.healspan.claimservice.mstupload.claim.dao.business;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "question_answer")
public class QuestionAnswer {
    @Id
    @SequenceGenerator(name="question_answer_generator", sequenceName = "question_answer_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "question_answer_generator")
    @Column(name="id")
    private Long id;

    @Column(name = "questions")
    private String question;

    @Column(name = "answers")
    private String answer;

    @ManyToOne(fetch = FetchType.LAZY)
    private ClaimStageLink claimStageLink;
}
