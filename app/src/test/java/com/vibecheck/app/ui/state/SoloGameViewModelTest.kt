package com.vibecheck.app.ui.state

import androidx.lifecycle.SavedStateHandle
import com.vibecheck.app.domain.SessionCodec
import com.vibecheck.app.domain.model.GameMode
import com.vibecheck.app.domain.solo.PersonaAnswer
import org.junit.Assert.*
import org.junit.Test

class SoloGameViewModelTest {
 @Test fun `valid solo party starts game with human participant awaiting answer`(){val vm=GameViewModel(SavedStateHandle());vm.startSoloSession(listOf("zendaya","nova"),GameMode.WHO_OF_US,42L,"Alex");assertTrue(vm.isSoloSession.value);assertEquals("Alex",vm.soloPlayerName.value);assertEquals("",vm.soloPlayerAnswer.value);assertEquals(AppScreen.GAME.name,vm.screenName.value)}
 @Test fun `solo session rejects fewer than two personas`(){val vm=GameViewModel(SavedStateHandle());vm.startSoloSession(listOf("nova"),GameMode.WHO_OF_US,42L,"Moi");assertFalse(vm.isSoloSession.value)}
 @Test fun `human must answer before simulated round can be submitted`(){val vm=GameViewModel(SavedStateHandle());vm.startSoloSession(listOf("zendaya","nova"),GameMode.WHO_OF_US,42L,"Moi");val answers=listOf(PersonaAnswer("zendaya","Nova",70,true),PersonaAnswer("nova","Nova",65,false));vm.submitSoloRound("q1",answers,false);assertTrue(SessionCodec.decodeVotes(vm.savedVotes.value).isEmpty());vm.submitSoloPlayerAnswer("Moi");assertEquals("Moi",vm.soloPlayerAnswer.value);vm.submitSoloRound("q1",answers,false);assertEquals(1,SessionCodec.decodeVotes(vm.savedVotes.value).size);assertEquals("",vm.soloPlayerAnswer.value)}
 @Test fun `solo result combines human and simulated answers in global vote`(){val vm=GameViewModel(SavedStateHandle());vm.startSoloSession(listOf("zendaya","nova"),GameMode.WHO_OF_US,42L,"Moi");vm.submitSoloPlayerAnswer("Moi");vm.submitSoloRound("q1",listOf(PersonaAnswer("zendaya","Nova",70,true),PersonaAnswer("nova","Nova",65,false)),false);assertEquals("Nova",SessionCodec.decodeVotes(vm.savedVotes.value).single().answer)}
 @Test fun `active solo session survives recreation with player identity`(){val state=SavedStateHandle();GameViewModel(state).startSoloSession(listOf("zendaya","nova"),GameMode.MOST_LIKELY,99L,"Alex");val restored=GameViewModel(state);assertTrue(restored.isSoloSession.value);assertEquals("Alex",restored.soloPlayerName.value);assertEquals(99L,restored.sessionSeed.value)}
}
