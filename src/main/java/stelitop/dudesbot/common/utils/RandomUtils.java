package stelitop.dudesbot.common.utils;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Random;

@Service
public class RandomUtils {

    @Autowired
    private Random random;

    /**
     * Randomly chooses an element from a list, where each element is weighted.
     * There must be at least one element in the list and the weights must be
     * positive.
     *
     * @param weights A collection of element-weight pairs.
     * @param <T> The type of the return element.
     * @return The randomly selected element.
     * @throws IllegalArgumentException Thrown if the list of weights is empty or
     * not all weights are positive.
     */
    public <T> T pickRandomlyWithWeights(@NonNull Collection<Pair<T, Integer>> weights) throws IllegalArgumentException {
        if (weights.isEmpty()) throw new IllegalArgumentException("The weights list must not be empty.");
        if (weights.stream().anyMatch(x -> x.getSecond() <= 0)) throw new IllegalArgumentException("There cannot be non-positive weights");
        int totalSum = weights.stream().mapToInt(Pair::getSecond).sum();
        int curSum = 0;
        int choice = random.nextInt(totalSum);
        for (var weight : weights) {
            curSum += weight.getSecond();
            if (choice < curSum) return weight.getFirst();
        }
        return null;
    }
}
