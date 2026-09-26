package com.vibecheck.app.data

import com.vibecheck.app.domain.solo.PersonaKind
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PersonaCatalogTest {
    @Test fun `solo catalog contains only public simulations`() { val public=PersonaCatalog.soloPublic; assertTrue(public.isNotEmpty()); assertTrue(public.all{it.kind==PersonaKind.PUBLIC_SIMULATION}); assertTrue(public.all{it.isFictionalSimulation}) }
    @Test fun `solo public roster stays recognizable and compact`() { val public=PersonaCatalog.soloPublic; assertTrue(public.size in 30..40); val required=setOf("beyonce","taylor_swift","rihanna","zendaya","dwayne_johnson","leonardo_dicaprio","lady_gaga","billie_eilish"); assertTrue(public.map{it.id}.containsAll(required)) }
    @Test fun `persona ids and public display names are unique`() { val personas=PersonaCatalog.all; assertEquals(personas.size,personas.map{it.id}.toSet().size); val public=PersonaCatalog.soloPublic; assertEquals(public.size,public.map{it.displayName}.toSet().size) }
    @Test fun `all persona traits stay inside supported scale`() { PersonaCatalog.all.forEach{p->val values=p.traits.values();assertEquals(8,values.size);assertTrue(values.all{it in 0..100})} }
    @Test fun `public entries keep nonempty simulation archetypes`() { assertTrue(PersonaCatalog.soloPublic.all{it.archetype.isNotBlank()}) }
}
