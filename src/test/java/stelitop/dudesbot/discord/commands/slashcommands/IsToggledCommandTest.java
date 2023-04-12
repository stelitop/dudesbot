package stelitop.dudesbot.discord.commands.slashcommands;

import discord4j.common.util.Snowflake;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import discord4j.core.object.command.Interaction;
import discord4j.core.object.entity.User;
import discord4j.core.spec.InteractionApplicationCommandCallbackReplyMono;
import discord4j.discordjson.json.UserData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;
import stelitop.dudesbot.database.services.UserProfileService;
import stelitop.dudesbot.discord.commands.slashcommands.annotations.Option;
import stelitop.dudesbot.game.entities.UserProfile;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalMatchers.and;
import static org.mockito.Mockito.*;

@SpringBootTest
class IsToggledCommandTest {

    @Mock
    private UserProfileService userProfileServiceMock;
    @InjectMocks
    private IsToggledCommand command;

    @Test
    void getNames() {
        var names = command.getNames();
        assertEquals(1, names.length);
        assertEquals("istoggled", names[0]);
    }

    @ParameterizedTest()
    @ValueSource(booleans = {true, false})
    void handleUserParticipatesWithProfileAlreadyInDatabase(boolean expectedResult) {
        ChatInputInteractionEvent eventMock = mock(ChatInputInteractionEvent.class);
        var eventReplyMock = mock(InteractionApplicationCommandCallbackReplyMono.class);
        when(eventMock.reply()).thenReturn(eventReplyMock);

        SlashCommandOptions optionsMock = mock(SlashCommandOptions.class);
        var retOptionMock = mock(ApplicationCommandInteractionOption.class);
        when(optionsMock.getOption("user")).thenReturn(Optional.of(retOptionMock));
        var retValueMock = mock(ApplicationCommandInteractionOptionValue.class);
        when(retOptionMock.getValue()).thenReturn(Optional.of(retValueMock));
        var userMock = mock(User.class);
        when(retValueMock.asUser()).thenReturn(Mono.just(userMock));
        long userId = 1234;
        String username = "frog";
        when(userMock.getUsername()).thenReturn(username);
        when(userMock.getId()).thenReturn(Snowflake.of(userId));
        //doReturn(Snowflake.of(userId)).when(userMock).getId();
        UserProfile userProfileMock = mock(UserProfile.class);
        when(userProfileServiceMock.getUserProfile(userId)).thenReturn(userProfileMock);
        when(userProfileMock.isParticipating()).thenReturn(expectedResult);

        var ret = command.handle(eventMock, optionsMock);

        String expectedText = expectedResult ? "opted in" : "opted out";
        verify(eventMock, times(1)).reply(anyString());
        verify(eventMock, times(1)).reply(and(contains(username), contains(expectedText)));
        verify(userProfileServiceMock, times(1)).getUserProfile(userId);
    }

    @ParameterizedTest()
    @ValueSource(booleans = {true, false})
    void handleUserParticipatesWithProfileNotYetInDatabase(boolean expectedResult) {
        ChatInputInteractionEvent eventMock = mock(ChatInputInteractionEvent.class);
        var eventReplyMock = mock(InteractionApplicationCommandCallbackReplyMono.class);
        when(eventMock.reply()).thenReturn(eventReplyMock);

        SlashCommandOptions optionsMock = mock(SlashCommandOptions.class);
        var retOptionMock = mock(ApplicationCommandInteractionOption.class);
        when(optionsMock.getOption("user")).thenReturn(Optional.empty());
        var userMock = mock(User.class);

        var interactionMock = mock(Interaction.class);
        when(eventMock.getInteraction()).thenReturn(interactionMock);
        when(interactionMock.getUser()).thenReturn(userMock);
        long userId = 1234;
        String username = "frog";
        when(userMock.getUsername()).thenReturn(username);
        when(userMock.getId()).thenReturn(Snowflake.of(userId));
        //doReturn(Snowflake.of(userId)).when(userMock).getId();
        UserProfile userProfileMock = mock(UserProfile.class);
        when(userProfileServiceMock.getUserProfile(userId)).thenReturn(userProfileMock);
        when(userProfileMock.isParticipating()).thenReturn(expectedResult);

        var ret = command.handle(eventMock, optionsMock);

        String expectedText = expectedResult ? "opted in" : "opted out";
        verify(eventMock, times(1)).reply(anyString());
        verify(eventMock, times(1)).reply(and(contains(username), contains(expectedText)));
        verify(userProfileServiceMock, times(1)).getUserProfile(userId);
    }
}