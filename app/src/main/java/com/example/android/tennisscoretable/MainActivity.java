package com.example.android.tennisscoretable;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    boolean finished = false;
    int serving = 0;
    boolean tiebreak = false;
    int[] challengesLeft = new int[2];
    Score[] score = new Score[2];
    View[] scoreViews = new View[2];
    TextView[] setsScore = new TextView[10];
    TextView[] pointsScore = new TextView[2];
    View[] buttonLayout = new View[2];
    View[] playerPresentation = new View[2];
    ImageView[] server = new ImageView[2];
    ImageView[] challenges = new ImageView[8];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize global variables
        scoreViews[0] = findViewById(R.id.player1Score);
        scoreViews[1] = findViewById(R.id.player2Score);

        setsScore[0] = (TextView) scoreViews[0].findViewById(R.id.set1Score);
        setsScore[1] = (TextView) scoreViews[0].findViewById(R.id.set2Score);
        setsScore[2] = (TextView) scoreViews[0].findViewById(R.id.set3Score);
        setsScore[3] = (TextView) scoreViews[0].findViewById(R.id.set4Score);
        setsScore[4] = (TextView) scoreViews[0].findViewById(R.id.set5Score);

        setsScore[5] = (TextView) scoreViews[1].findViewById(R.id.set1Score);
        setsScore[6] = (TextView) scoreViews[1].findViewById(R.id.set2Score);
        setsScore[7] = (TextView) scoreViews[1].findViewById(R.id.set3Score);
        setsScore[8] = (TextView) scoreViews[1].findViewById(R.id.set4Score);
        setsScore[9] = (TextView) scoreViews[1].findViewById(R.id.set5Score);

        pointsScore[0] = (TextView) scoreViews[0].findViewById(R.id.gameScore);
        pointsScore[1] = (TextView) scoreViews[1].findViewById(R.id.gameScore);

        buttonLayout[0] = findViewById(R.id.playerOneButtons);
        buttonLayout[1] = findViewById(R.id.playerTwoButtons);

        playerPresentation[0] = findViewById(R.id.playerOnePresentationLayout);
        playerPresentation[1] = findViewById(R.id.playerTwoPresentationLayout);

        challenges[0] = (ImageView) playerPresentation[0].findViewById(R.id.challengeOneView);
        challenges[1] = (ImageView) playerPresentation[0].findViewById(R.id.challengeTwoView);
        challenges[2] = (ImageView) playerPresentation[0].findViewById(R.id.challengeThreeView);
        challenges[3] = (ImageView) playerPresentation[0].findViewById(R.id.tiebreakBonusChallengeView);

        challenges[4] = (ImageView) playerPresentation[1].findViewById(R.id.challengeOneView);
        challenges[5] = (ImageView) playerPresentation[1].findViewById(R.id.challengeTwoView);
        challenges[6] = (ImageView) playerPresentation[1].findViewById(R.id.challengeThreeView);
        challenges[7] = (ImageView) playerPresentation[1].findViewById(R.id.tiebreakBonusChallengeView);

        score[0] = new Score();
        score[1] = new Score();

        challengesLeft[0] = 3;
        challengesLeft[1] = 3;

        // Set first player presentation layout
        ImageView playerImage = (ImageView) playerPresentation[0].findViewById(R.id.playerImageView);
        playerImage.setImageResource(R.drawable.federer_full_15);
        TextView playerName = (TextView) playerPresentation[0].findViewById(R.id.playerTextView);
        playerName.setText(R.string.playerOneName);

        // Set second player presentation layout
        playerImage = (ImageView) playerPresentation[1].findViewById(R.id.playerImageView);
        playerImage.setImageResource(R.drawable.nadal_full_15);
        playerName = (TextView) playerPresentation[1].findViewById(R.id.playerTextView);
        playerName.setText(R.string.playerTwoName);

        // Set first player score table
        ImageView flag = (ImageView) scoreViews[0].findViewById(R.id.playerFlagImage);
        flag.setImageResource(R.drawable.swiss_flag);
        playerName = (TextView) scoreViews[0].findViewById(R.id.playerNameScoreView);
        playerName.setText(R.string.playerOneScoreName);
        server[0] = (ImageView) scoreViews[0].findViewById(R.id.servingImage);
        server[0].setImageResource(R.drawable.left_arrow);

        // Set second player score table
        scoreViews[1] = findViewById(R.id.player2Score);
        flag = (ImageView) scoreViews[1].findViewById(R.id.playerFlagImage);
        flag.setImageResource(R.drawable.spain_flag);
        playerName = (TextView) scoreViews[1].findViewById(R.id.playerNameScoreView);
        playerName.setText(R.string.playerTwoScoreName);
        server[1] = (ImageView) scoreViews[1].findViewById(R.id.servingImage);
    }

    public void increaseScore(View view) {

        if (!finished) {
            int parentId = ((View) view.getParent()).getId();

            if (parentId == R.id.playerOneButtons) {
                computeScore(0);
            } else if (parentId == R.id.playerTwoButtons) {
                computeScore(1);
            }
        }
    }

    private void computeScore(int player) {

        int opponent = 1 - player;

        if (tiebreak) {
            if (score[player].getPoints() < 6) {
                // Player 0, 1, 2, 3, 4 or 5 points
                displayPoints(player, score[player].getPoints() + 1);
            } else if (score[player].getPoints() >= 6) {
                // Player 6 or more points
                if (score[opponent].getPoints() < score[player].getPoints()) {
                    // Opponent 1 or more points less than Player
                    resetPoints(player, opponent);
                    nextSet(player, opponent);
                    if (score[player].getSets() == 3) {
                        // Player 3 sets
                        finished = true;
                    }
                    tiebreak = false;
                } else if (score[opponent].getPoints() >= score[player].getPoints()) {
                    // Opponent equal or more points than Player
                    displayPoints(player, score[player].getPoints() + 1);
                }
            }
        } else {
            if (score[player].getPoints() < 3) {
                // Player 0, 15 or 30 points
                displayPoints(player, score[player].getPoints() + 1);
            } else if (score[player].getPoints() == 3) {
                // Player 40 points
                if (score[opponent].getPoints() < 3) {
                    // Opponent 0, 15 or 30 points
                    resetPoints(player, opponent);
                    checkScore(player, opponent);
                } else if (score[opponent].getPoints() == 3) {
                    // Opponent 40 points
                    displayPoints(player, 4);
                } else if (score[opponent].getPoints() == 4) {
                    // Opponent Advantage
                    displayPoints(opponent, 3);
                    displayPoints(player, 3);
                }
            } else if (score[player].getPoints() == 4) {
                // Player Advantage
                resetPoints(player, opponent);
                checkScore(player, opponent);
            }
        }
    }

    private void checkScore(int player, int opponent) {

        if (score[player].getGames() < 5) {
            // Player 0, 1, 2, 3 or 4 games
            displaySets(player, score[player].getSets() + score[opponent].getSets(), score[player].getGames() + 1);
        } else if (score[player].getGames() == 5) {
            // Player 5 games
            if (score[opponent].getGames() < 5) {
                // Opponent 0, 1, 2, 3 or 4 games
                nextSet(player, opponent);
                if (score[player].getSets() == 3) {
                    // Player 3 sets
                    finished = true;
                }
            } else if (score[opponent].getGames() == 5) {
                // Opponent 5 games
                displaySets(player, score[player].getSets() + score[opponent].getSets(), score[player].getGames() + 1);
            } else if (score[opponent].getGames() == 6) {
                // Opponent 6 games
                displaySets(player, score[player].getSets() + score[opponent].getSets(), score[player].getGames() + 1);
                tiebreak = true;
                giveBonusChallenge(player);
                giveBonusChallenge(opponent);
            }
        } else if (score[player].getGames() == 6) {
            // Player 6 games, Opponent 5 games
            nextSet(player, opponent);
            if (score[player].getSets() == 3) {
                // Player 3 sets
                finished = true;
            }
        }
    }

    public void resetPoints(int player, int opponent) {

        score[player].setPoints(0);
        displayPoints(player, score[player].getPoints());
        score[opponent].setPoints(0);
        displayPoints(opponent, score[opponent].getPoints());

        serving = 1 - serving;
        server[serving].setImageResource(R.drawable.left_arrow);
        server[1 - serving].setImageResource(0);
    }

    public void nextSet(int player, int opponent) {

        int totalSets = score[player].getSets() + score[opponent].getSets();

        displaySets(player, totalSets, score[player].getGames() + 1);
        setsScore[totalSets + 5 * player].setTextColor(getResources().getColor(R.color.colorSetWon));
        score[player].setGames(0);
        score[opponent].setGames(0);
        score[player].setSets(score[player].getSets() + 1);

        if (totalSets < 4) {
            setsScore[totalSets + 1].setVisibility(View.VISIBLE);
            setsScore[totalSets + 6].setVisibility(View.VISIBLE);

            resetChallenges(player);
            resetChallenges(opponent);
        }
    }

    private void displayPoints(int player, int points) {

        int totalPoints = points + score[1 - player].getPoints();

        score[player].setPoints(points);

        if (tiebreak) {
            pointsScore[player].setText(String.valueOf(points));

            if (totalPoints % 2 == 1) {
                serving = 1 - serving;
                server[serving].setImageResource(R.drawable.left_arrow);
                server[1 - serving].setImageResource(0);
            }
        } else {
            if (points == 0) {
                pointsScore[player].setText("0");
            } else if (points == 1) {
                pointsScore[player].setText(R.string.onePoint);
            }
            if (points == 2) {
                pointsScore[player].setText(R.string.twoPoints);
            }
            if (points == 3) {
                pointsScore[player].setText(R.string.threePoints);
            }
            if (points == 4) {
                pointsScore[player].setText(R.string.fourPoints);
                pointsScore[1 - player].setText(R.string.nothing);
            }
        }
    }

    private void displaySets(int player, int set, int games) {

        score[player].setGames(games);
        setsScore[set + 5 * player].setText(String.valueOf(games));
    }

    public void challenge(View view) {

        if (!finished) {
            int parentId = ((View) view.getParent()).getId();

            if (parentId == R.id.playerOneButtons && challengesLeft[0] > 0) {
                makeChallenge(0);
            } else if (parentId == R.id.playerTwoButtons && challengesLeft[1] > 0) {
                makeChallenge(1);
            }
        }
    }

    private void makeChallenge(int player) {

        int opponent = 1 - player;

        Random r = new Random();
        int max = 15, min = 0;
        int randomNumberA = r.nextInt(max - min + 1) + min;
        max = 50;
        int randomNumberB = r.nextInt(max - min + 1) + min;

        if (randomNumberA <= 10 && randomNumberB <= 35) {
            computeScore(player);
        } else {
            computeScore(opponent);
            challenges[player * 4 + --challengesLeft[player]].setVisibility(View.INVISIBLE);
        }

    }

    private void resetChallenges(int player) {

        for (int i = challengesLeft[player] + player * 4; i < 3 + player * 4; i++) {
            challenges[i].setVisibility(View.VISIBLE);
        }

        if (challengesLeft[player] == 4) {
            challenges[player * 4 + 3].setVisibility(View.INVISIBLE);
        }

        challengesLeft[player] = 3;
    }

    private void giveBonusChallenge(int player) {
        challenges[player * 4 + challengesLeft[player]++].setVisibility(View.VISIBLE);
    }

    public class Score {
        private int sets, games, points;

        Score() {
            this.sets = 0;
            this.games = 0;
            this.points = 0;
        }

        void setSets(int sets) {
            this.sets = sets;
        }

        int getSets() {
            return sets;
        }

        void setGames(int games) {
            this.games = games;
        }

        int getGames() {
            return games;
        }

        void setPoints(int points) {
            this.points = points;
        }

        int getPoints() {
            return points;
        }

    }
}
