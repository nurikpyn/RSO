package org.voting.gateway.domain;

import java.util.Date;
import java.util.UUID;

import com.datastax.driver.core.utils.UUIDs;
import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

@Table(name = "voting_report",keyspace = "rso",
caseSensitiveKeyspace = false,
caseSensitiveTable = false)
public class VotingReport {

    @PartitionKey
    @Column(name = "report_id")
    private UUID id;

    @Column(name = "candidate_id")
    private UUID candidateId;

    @Column(name = "candidate_name")
    private String candidateName;

    @Column(name = "candidate_surname")
    private String candidateSurname;

    @Column(name = "commune_name")
    private String communeName;

    @Column(name = "date_generated")
    private Date dateGenerated;

    @Column(name = "is_voting_finished")
    private boolean votingFinished;

    @Column(name = "no_can_vote")
    private int noCanVote;

    @Column(name = "no_cards_used")
    private int noCardsUsed;

    @Column(name = "no_of_votes")
    private int noOfVotes;

    @Column(name = "no_turn")
    private int noTurn;

    @Column(name = "turn_id")
    private UUID turnId;

    @Column(name = "vote_type")
    private String voteType;

    @Column(name = "ward_id")
    private UUID wardId;

    @Column(name = "ward_name")
    private String wardName;

    public VotingReport() {
    }

    public VotingReport(UUID districtId, UUID round, VotingData votingData, String type) {

        this.id = UUIDs.timeBased();

        this.wardId = districtId;
        this.turnId = round;
        this.noCanVote = votingData.getNoCanVote();
        this.noCardsUsed = votingData.getNoCardsUsed();
        this.voteType = type;
        this.dateGenerated = new Date();



    }

    public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}



	public String getCandidateName() {
		return candidateName;
	}

	public void setCandidateName(String candidateName) {
		this.candidateName = candidateName;
	}

	public String getCandidateSurname() {
		return candidateSurname;
	}

	public void setCandidateSurname(String candidateSurname) {
		this.candidateSurname = candidateSurname;
	}

    public UUID getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(UUID candidateId) {
        this.candidateId = candidateId;
    }

    public String getCommuneName() {
        return communeName;
    }

    public void setCommuneName(String communeName) {
        this.communeName = communeName;
    }

    public UUID getTurnId() {
        return turnId;
    }

    public void setTurnId(UUID turnId) {
        this.turnId = turnId;
    }

    public UUID getWardId() {
        return wardId;
    }

    public void setWardId(UUID wardId) {
        this.wardId = wardId;
    }

    public Date getDateGenerated() {
		return dateGenerated;
	}

	public void setDateGenerated(Date dateGenerated) {
		this.dateGenerated = dateGenerated;
	}

    public boolean isVotingFinished() {
        return votingFinished;
    }

    public void setVotingFinished(boolean votingFinished) {
        this.votingFinished = votingFinished;
    }

    public int getNoCanVote() {
		return noCanVote;
	}

	public void setNoCanVote(int noCanVote) {
		this.noCanVote = noCanVote;
	}

	public int getNoCardsUsed() {
		return noCardsUsed;
	}

	public void setNoCardsUsed(int noCardsUsed) {
		this.noCardsUsed = noCardsUsed;
	}

	public int getNoOfVotes() {
		return noOfVotes;
	}

	public void setNoOfVotes(int noOfVotes) {
		this.noOfVotes = noOfVotes;
	}

	public int getNoTurn() {
		return noTurn;
	}

	public void setNoTurn(int noTurn) {
		this.noTurn = noTurn;
	}

	public String getVoteType() {
		return voteType;
	}

	public void setVoteType(String voteType) {
		this.voteType = voteType;
	}

	public String getWardName() {
		return wardName;
	}

	public void setWardName(String wardName) {
		this.wardName = wardName;
	}


}
