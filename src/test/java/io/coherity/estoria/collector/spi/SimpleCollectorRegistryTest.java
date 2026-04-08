package io.coherity.estoria.collector.spi;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class SimpleCollectorRegistryTest
{
    @Nested
    class ConstructorTest
    {
        @Test
        void givenNewCollectorRegistry_whenConstructed_thenRegistryIsEmpty()
        {
            CollectorRegistry registry = new SimpleCollectorRegistry();

            assertThat(registry.size()).isZero();
            assertThat(registry.getRegisteredCollectors()).isEmpty();
            assertThat(registry.getRegisteredEntityTypes()).isEmpty();
            assertThat(registry.getKnownEntityTypes()).isEmpty();
            assertThat(registry.getUnresolvedEntityTypes()).isEmpty();
        }
    }

    @Nested
    class RegisterTest
    {
        @Test
        void givenNullCollector_whenRegisterCalled_thenRegistryRemainsUnchanged()
        {
            CollectorRegistry registry = new SimpleCollectorRegistry();

            registry.register(null);

            assertThat(registry.size()).isZero();
            assertThat(registry.getRegisteredCollectors()).isEmpty();
            assertThat(registry.getRegisteredEntityTypes()).isEmpty();
            assertThat(registry.getKnownEntityTypes()).isEmpty();
            assertThat(registry.getUnresolvedEntityTypes()).isEmpty();
        }

        @Test
        void givenCollectorWithNullCollectorInfo_whenRegisterCalled_thenRegistryRemainsUnchanged()
        {
            CollectorRegistry registry = new SimpleCollectorRegistry();
            Collector collector = Mockito.mock(Collector.class);
            Mockito.when(collector.getCollectorInfo()).thenReturn(null);

            registry.register(collector);

            assertThat(registry.size()).isZero();
            assertThat(registry.getRegisteredCollectors()).isEmpty();
            assertThat(registry.getRegisteredEntityTypes()).isEmpty();
            assertThat(registry.getKnownEntityTypes()).isEmpty();
            assertThat(registry.getUnresolvedEntityTypes()).isEmpty();
        }

        @Test
        void givenCollectorWithNullEntityType_whenRegisterCalled_thenRegistryRemainsUnchanged()
        {
            CollectorRegistry registry = new SimpleCollectorRegistry();
            Collector collector = mockCollector(null, Set.of("dependency"));

            registry.register(collector);

            assertThat(registry.size()).isZero();
            assertThat(registry.getRegisteredCollectors()).isEmpty();
            assertThat(registry.getRegisteredEntityTypes()).isEmpty();
            assertThat(registry.getKnownEntityTypes()).isEmpty();
            assertThat(registry.getUnresolvedEntityTypes()).isEmpty();
        }

        @Test
        void givenCollectorWithBlankEntityType_whenRegisterCalled_thenRegistryRemainsUnchanged()
        {
            CollectorRegistry registry = new SimpleCollectorRegistry();
            Collector collector = mockCollector("   ", Set.of("dependency"));

            registry.register(collector);

            assertThat(registry.size()).isZero();
            assertThat(registry.getRegisteredCollectors()).isEmpty();
            assertThat(registry.getRegisteredEntityTypes()).isEmpty();
            assertThat(registry.getKnownEntityTypes()).isEmpty();
            assertThat(registry.getUnresolvedEntityTypes()).isEmpty();
        }

        @Test
        void givenCollectorWithNullDependencies_whenRegisterCalled_thenCollectorIsRegisteredWithNoDependencies()
        {
            CollectorRegistry registry = new SimpleCollectorRegistry();
            Collector collector = mockCollector("instance", null);

            registry.register(collector);

            assertThat(registry.size()).isEqualTo(1);
            Collector foundCollector = registry.getRegisteredCollector("instance").orElseThrow();
            assertThat(foundCollector).isSameAs(collector);
            assertThat(registry.getRegisteredEntityTypes()).containsExactly("instance");
            assertThat(registry.getDependencies("instance")).isEmpty();
            assertThat(registry.getKnownEntityTypes()).containsExactly("instance");
            assertThat(registry.getUnresolvedEntityTypes()).isEmpty();
        }

        @Test
        void givenCollectorWithDependencies_whenRegisterCalled_thenCollectorAndDependenciesAreTracked()
        {
            CollectorRegistry registry = new SimpleCollectorRegistry();
            Collector collector = mockCollector("instance", Set.of("subnet", "vpc"));

            registry.register(collector);

            assertThat(registry.size()).isEqualTo(1);
            Collector foundCollector = registry.getRegisteredCollector("instance").orElseThrow();
            assertThat(foundCollector).isSameAs(collector);
            assertThat(registry.getRegisteredEntityTypes()).containsExactly("instance");
            assertThat(registry.getDependencies("instance")).containsExactlyInAnyOrder("subnet", "vpc");
            assertThat(registry.getKnownEntityTypes()).containsExactlyInAnyOrder("instance", "subnet", "vpc");
            assertThat(registry.getUnresolvedEntityTypes()).containsExactlyInAnyOrder("subnet", "vpc");
        }

        @Test
        void givenCollectorWithNullAndBlankDependencies_whenRegisterCalled_thenOnlyValidDependenciesAreTracked()
        {
            CollectorRegistry registry = new SimpleCollectorRegistry();
            LinkedHashSet<String> dependencies = new LinkedHashSet<>();
            dependencies.add("vpc");
            dependencies.add(null);
            dependencies.add("   ");
            dependencies.add("subnet");

            Collector collector = mockCollector("instance", dependencies);

            registry.register(collector);

            assertThat(registry.getDependencies("instance")).containsExactly("vpc", "subnet");
            assertThat(registry.getKnownEntityTypes()).containsExactly("instance", "vpc", "subnet");
            assertThat(registry.getUnresolvedEntityTypes()).containsExactly("vpc", "subnet");
        }

        @Test
        void givenCollectorWithExistingEntityType_whenRegisterCalled_thenCollectorAndDependenciesAreOverwrittenButKnownEntityTypesRemainAccumulated()
        {
            CollectorRegistry registry = new SimpleCollectorRegistry();
            Collector originalCollector = mockCollector("instance", Set.of("vpc"));
            Collector replacementCollector = mockCollector("instance", Set.of("subnet"));

            registry.register(originalCollector);
            registry.register(replacementCollector);

            Collector foundCollector = registry.getRegisteredCollector("instance").orElseThrow();

            assertThat(registry.size()).isEqualTo(1);
            assertThat(foundCollector).isSameAs(replacementCollector);
            assertThat(registry.getDependencies("instance")).containsExactly("subnet");
            assertThat(registry.getKnownEntityTypes()).containsExactly("instance", "vpc", "subnet");
            assertThat(registry.getUnresolvedEntityTypes()).containsExactly("vpc", "subnet");
        }
    }

    @Nested
    class RegisterAllTest
    {
        @Test
        void givenNullCollectorsCollection_whenRegisterAllCalled_thenRegistryRemainsUnchanged()
        {
            CollectorRegistry registry = new SimpleCollectorRegistry();

            registry.registerAll(null);

            assertThat(registry.size()).isZero();
            assertThat(registry.getRegisteredCollectors()).isEmpty();
            assertThat(registry.getRegisteredEntityTypes()).isEmpty();
            assertThat(registry.getKnownEntityTypes()).isEmpty();
            assertThat(registry.getUnresolvedEntityTypes()).isEmpty();
        }

        @Test
        void givenEmptyCollectorsCollection_whenRegisterAllCalled_thenRegistryRemainsUnchanged()
        {
            CollectorRegistry registry = new SimpleCollectorRegistry();

            registry.registerAll(List.of());

            assertThat(registry.size()).isZero();
            assertThat(registry.getRegisteredCollectors()).isEmpty();
            assertThat(registry.getRegisteredEntityTypes()).isEmpty();
            assertThat(registry.getKnownEntityTypes()).isEmpty();
            assertThat(registry.getUnresolvedEntityTypes()).isEmpty();
        }

        @Test
        void givenValidCollectors_whenRegisterAllCalled_thenAllCollectorsAreRegistered()
        {
            CollectorRegistry registry = new SimpleCollectorRegistry();
            Collector vpcCollector = mockCollector("vpc", Set.of());
            Collector subnetCollector = mockCollector("subnet", Set.of("vpc"));
            Collector instanceCollector = mockCollector("instance", Set.of("subnet"));

            registry.registerAll(List.of(vpcCollector, subnetCollector, instanceCollector));

            assertThat(registry.size()).isEqualTo(3);
            assertThat(registry.getRegisteredEntityTypes()).containsExactly("vpc", "subnet", "instance");
            assertThat(registry.getKnownEntityTypes()).containsExactly("vpc", "subnet", "instance");
            assertThat(registry.getUnresolvedEntityTypes()).isEmpty();
        }

        @Test
        void givenCollectorsContainingNullAndInvalidEntries_whenRegisterAllCalled_thenOnlyValidCollectorsAreRegistered()
        {
            CollectorRegistry registry = new SimpleCollectorRegistry();
            Collector validCollector = mockCollector("vpc", Set.of());
            Collector invalidCollector = mockCollector(" ", Set.of("subnet"));
            Collector noInfoCollector = Mockito.mock(Collector.class);
            Mockito.when(noInfoCollector.getCollectorInfo()).thenReturn(null);

            registry.registerAll(Arrays.asList(null, invalidCollector, noInfoCollector, validCollector));

            assertThat(registry.size()).isEqualTo(1);
            assertThat(registry.getRegisteredEntityTypes()).containsExactly("vpc");
            assertThat(registry.getKnownEntityTypes()).containsExactly("vpc");
            assertThat(registry.getUnresolvedEntityTypes()).isEmpty();
        }
    }

    @Nested
    class GetRegisteredEntityTypesTest
    {
        @Test
        void givenNoCollectorsRegistered_whenGetRegisteredEntityTypesCalled_thenEmptySetIsReturned()
        {
            CollectorRegistry registry = new SimpleCollectorRegistry();

            Set<String> result = registry.getRegisteredEntityTypes();

            assertThat(result).isEmpty();
        }

        @Test
        void givenCollectorsRegistered_whenGetRegisteredEntityTypesCalled_thenRegisteredEntityTypesAreReturnedInInsertionOrder()
        {
            CollectorRegistry registry = new SimpleCollectorRegistry();
            registry.register(mockCollector("vpc", Set.of()));
            registry.register(mockCollector("subnet", Set.of("vpc")));

            Set<String> result = registry.getRegisteredEntityTypes();

            assertThat(result).containsExactly("vpc", "subnet");
        }

        @Test
        void givenRegisteredEntityTypesReturned_whenModified_thenUnsupportedOperationExceptionIsThrown()
        {
            CollectorRegistry registry = new SimpleCollectorRegistry();
            registry.register(mockCollector("vpc", Set.of()));

            Set<String> result = registry.getRegisteredEntityTypes();

            assertThatThrownBy(() -> result.add("subnet"))
                .isInstanceOf(UnsupportedOperationException.class);
        }
    }

    @Nested
    class GetRegisteredCollectorTest
    {
        @Test
        void givenNullEntityType_whenGetRegisteredCollectorCalled_thenEmptyOptionalIsReturned()
        {
            CollectorRegistry registry = new SimpleCollectorRegistry();

            Optional<Collector> result = registry.getRegisteredCollector(null);

            assertThat(result).isEmpty();
        }

        @Test
        void givenBlankEntityType_whenGetRegisteredCollectorCalled_thenEmptyOptionalIsReturned()
        {
            CollectorRegistry registry = new SimpleCollectorRegistry();

            Optional<Collector> result = registry.getRegisteredCollector("   ");

            assertThat(result).isEmpty();
        }

        @Test
        void givenUnknownEntityType_whenGetRegisteredCollectorCalled_thenEmptyOptionalIsReturned()
        {
            CollectorRegistry registry = new SimpleCollectorRegistry();

            Optional<Collector> result = registry.getRegisteredCollector("unknown");

            assertThat(result).isEmpty();
        }

        @Test
        void givenRegisteredEntityType_whenGetRegisteredCollectorCalled_thenMatchingCollectorIsReturned()
        {
            CollectorRegistry registry = new SimpleCollectorRegistry();
            Collector collector = mockCollector("instance", Set.of("subnet"));
            registry.register(collector);

            Optional<Collector> result = registry.getRegisteredCollector("instance");

            assertThat(result).containsSame(collector);
        }
    }

    @Nested
    class HasRegisteredCollectorTest
    {
        @Test
        void givenNullEntityType_whenHasRegisteredCollectorCalled_thenFalseIsReturned()
        {
            CollectorRegistry registry = new SimpleCollectorRegistry();

            boolean result = registry.hasRegisteredCollector(null);

            assertThat(result).isFalse();
        }

        @Test
        void givenBlankEntityType_whenHasRegisteredCollectorCalled_thenFalseIsReturned()
        {
            CollectorRegistry registry = new SimpleCollectorRegistry();

            boolean result = registry.hasRegisteredCollector("   ");

            assertThat(result).isFalse();
        }

        @Test
        void givenUnknownEntityType_whenHasRegisteredCollectorCalled_thenFalseIsReturned()
        {
            CollectorRegistry registry = new SimpleCollectorRegistry();

            boolean result = registry.hasRegisteredCollector("unknown");

            assertThat(result).isFalse();
        }

        @Test
        void givenRegisteredEntityType_whenHasRegisteredCollectorCalled_thenTrueIsReturned()
        {
            CollectorRegistry registry = new SimpleCollectorRegistry();
            registry.register(mockCollector("instance", Set.of()));

            boolean result = registry.hasRegisteredCollector("instance");

            assertThat(result).isTrue();
        }
    }

    @Nested
    class GetRegisteredCollectorsTest
    {
        @Test
        void givenNoCollectorsRegistered_whenGetRegisteredCollectorsCalled_thenEmptyCollectionIsReturned()
        {
            CollectorRegistry registry = new SimpleCollectorRegistry();

            Collection<Collector> result = registry.getRegisteredCollectors();

            assertThat(result).isEmpty();
        }

        @Test
        void givenCollectorsRegistered_whenGetRegisteredCollectorsCalled_thenRegisteredCollectorsAreReturnedInInsertionOrder()
        {
            CollectorRegistry registry = new SimpleCollectorRegistry();
            Collector vpcCollector = mockCollector("vpc", Set.of());
            Collector subnetCollector = mockCollector("subnet", Set.of("vpc"));

            registry.register(vpcCollector);
            registry.register(subnetCollector);

            Collection<Collector> result = registry.getRegisteredCollectors();

            assertThat(result).containsExactly(vpcCollector, subnetCollector);
        }

        @Test
        void givenRegisteredCollectorsReturned_whenModified_thenUnsupportedOperationExceptionIsThrown()
        {
            CollectorRegistry registry = new SimpleCollectorRegistry();
            Collector collector = mockCollector("vpc", Set.of());
            registry.register(collector);

            Set<Collector> result = registry.getRegisteredCollectors();

            assertThatThrownBy(() -> result.add(mockCollector("subnet", Set.of("vpc"))))
                .isInstanceOf(UnsupportedOperationException.class);
        }
    }

    @Nested
    class SizeTest
    {
        @Test
        void givenNoCollectorsRegistered_whenSizeCalled_thenZeroIsReturned()
        {
            CollectorRegistry registry = new SimpleCollectorRegistry();

            int result = registry.size();

            assertThat(result).isZero();
        }

        @Test
        void givenCollectorsRegistered_whenSizeCalled_thenCollectorCountIsReturned()
        {
            CollectorRegistry registry = new SimpleCollectorRegistry();
            registry.register(mockCollector("vpc", Set.of()));
            registry.register(mockCollector("subnet", Set.of("vpc")));

            int result = registry.size();

            assertThat(result).isEqualTo(2);
        }
    }

    @Nested
    class GetDependenciesTest
    {
        @Test
        void givenNullEntityType_whenGetDependenciesCalled_thenEmptySetIsReturned()
        {
            CollectorRegistry registry = new SimpleCollectorRegistry();

            Set<String> result = registry.getDependencies(null);

            assertThat(result).isEmpty();
        }

        @Test
        void givenBlankEntityType_whenGetDependenciesCalled_thenEmptySetIsReturned()
        {
            CollectorRegistry registry = new SimpleCollectorRegistry();

            Set<String> result = registry.getDependencies("   ");

            assertThat(result).isEmpty();
        }

        @Test
        void givenUnknownEntityType_whenGetDependenciesCalled_thenNullIsReturned()
        {
            CollectorRegistry registry = new SimpleCollectorRegistry();

            Set<String> result = registry.getDependencies("unknown");

            assertThat(result).isNull();
        }

        @Test
        void givenRegisteredEntityTypeWithNoDependencies_whenGetDependenciesCalled_thenEmptySetIsReturned()
        {
            CollectorRegistry registry = new SimpleCollectorRegistry();
            registry.register(mockCollector("vpc", Set.of()));

            Set<String> result = registry.getDependencies("vpc");

            assertThat(result).isEmpty();
        }

        @Test
        void givenRegisteredEntityTypeWithDependencies_whenGetDependenciesCalled_thenDependenciesAreReturnedInInsertionOrder()
        {
            CollectorRegistry registry = new SimpleCollectorRegistry();
            LinkedHashSet<String> dependencies = new LinkedHashSet<>();
            dependencies.add("vpc");
            dependencies.add("subnet");
            registry.register(mockCollector("instance", dependencies));

            Set<String> result = registry.getDependencies("instance");

            assertThat(result).containsExactly("vpc", "subnet");
        }

        @Test
        void givenDependenciesReturned_whenModified_thenUnsupportedOperationExceptionIsThrown()
        {
            CollectorRegistry registry = new SimpleCollectorRegistry();
            registry.register(mockCollector("instance", Set.of("subnet")));

            Set<String> result = registry.getDependencies("instance");

            assertThatThrownBy(() -> result.add("vpc"))
                .isInstanceOf(UnsupportedOperationException.class);
        }
    }

    @Nested
    class GetKnownEntityTypesComplementTest
    {
        @Test
        void givenNullEntityTypes_whenGetKnownEntityTypesComplementCalled_thenAllKnownEntityTypesAreReturned()
        {
            CollectorRegistry registry = new SimpleCollectorRegistry();
            registry.register(mockCollector("instance", Set.of("subnet", "vpc")));

            Set<String> result = registry.getKnownEntityTypesComplement(null);

            assertThat(result).containsExactlyInAnyOrder("instance", "subnet", "vpc");
        }

        @Test
        void givenEmptyEntityTypes_whenGetKnownEntityTypesComplementCalled_thenAllKnownEntityTypesAreReturned()
        {
            CollectorRegistry registry = new SimpleCollectorRegistry();
            registry.register(mockCollector("instance", Set.of("subnet", "vpc")));

            Set<String> result = registry.getKnownEntityTypesComplement(Set.of());

            assertThat(result).containsExactlyInAnyOrder("instance", "subnet", "vpc");
        }

        @Test
        void givenSubsetOfKnownEntityTypes_whenGetKnownEntityTypesComplementCalled_thenRemainingKnownEntityTypesAreReturned()
        {
            CollectorRegistry registry = new SimpleCollectorRegistry();
            registry.register(mockCollector("instance", Set.of("subnet", "vpc")));

            Set<String> result = registry.getKnownEntityTypesComplement(Set.of("instance", "vpc"));

            assertThat(result).containsExactly("subnet");
        }

        @Test
        void givenUnknownEntityTypesInInput_whenGetKnownEntityTypesComplementCalled_thenUnknownEntityTypesAreIgnored()
        {
            CollectorRegistry registry = new SimpleCollectorRegistry();
            registry.register(mockCollector("instance", Set.of("subnet")));

            Set<String> result = registry.getKnownEntityTypesComplement(Set.of("unknown"));

            assertThat(result).containsExactly("instance", "subnet");
        }
    }

    @Nested
    class GetKnownEntityTypesTest
    {
        @Test
        void givenNoCollectorsRegistered_whenGetKnownEntityTypesCalled_thenEmptySetIsReturned()
        {
            CollectorRegistry registry = new SimpleCollectorRegistry();

            Set<String> result = registry.getKnownEntityTypes();

            assertThat(result).isEmpty();
        }

        @Test
        void givenCollectorsRegistered_whenGetKnownEntityTypesCalled_thenKnownEntityTypesAreReturned()
        {
            CollectorRegistry registry = new SimpleCollectorRegistry();
            registry.register(mockCollector("instance", Set.of("subnet", "vpc")));

            Set<String> result = registry.getKnownEntityTypes();

            assertThat(result).containsExactlyInAnyOrder("instance", "subnet", "vpc");
        }

        @Test
        void givenKnownEntityTypesReturned_whenModified_thenRegistryStateIsUnaffected()
        {
            CollectorRegistry registry = new SimpleCollectorRegistry();
            registry.register(mockCollector("instance", Set.of("subnet")));

            Set<String> result = registry.getKnownEntityTypes();
            result.add("vpc");

            assertThat(registry.getKnownEntityTypes()).containsExactly("instance", "subnet");
        }
    }

    @Nested
    class GetUnresolvedEntityTypesTest
    {
        @Test
        void givenNoCollectorsRegistered_whenGetUnresolvedEntityTypesCalled_thenEmptySetIsReturned()
        {
            CollectorRegistry registry = new SimpleCollectorRegistry();

            Set<String> result = registry.getUnresolvedEntityTypes();

            assertThat(result).isEmpty();
        }

        @Test
        void givenDependenciesWithoutRegisteredCollectors_whenGetUnresolvedEntityTypesCalled_thenUnresolvedEntityTypesAreReturned()
        {
            CollectorRegistry registry = new SimpleCollectorRegistry();
            registry.register(mockCollector("instance", Set.of("subnet", "vpc")));

            Set<String> result = registry.getUnresolvedEntityTypes();

            assertThat(result).containsExactlyInAnyOrder("subnet", "vpc");
        }

        @Test
        void givenAllKnownEntityTypesHaveRegisteredCollectors_whenGetUnresolvedEntityTypesCalled_thenEmptySetIsReturned()
        {
            CollectorRegistry registry = new SimpleCollectorRegistry();
            registry.register(mockCollector("vpc", Set.of()));
            registry.register(mockCollector("subnet", Set.of("vpc")));
            registry.register(mockCollector("instance", Set.of("subnet")));

            Set<String> result = registry.getUnresolvedEntityTypes();

            assertThat(result).isEmpty();
        }
    }

    @Nested
    class GetExecutionOrderTest
    {
        @Test
        void givenSingleEntityTypeWithNoDependencies_whenGetExecutionOrderCalled_thenSingleEntityTypeIsReturned()
            throws CircularReferenceException
        {
            CollectorRegistry registry = new SimpleCollectorRegistry();
            registry.register(mockCollector("vpc", Set.of()));

            List<String> result = registry.getExecutionOrder("vpc");

            assertThat(result).containsExactly("vpc");
        }

        @Test
        void givenEntityTypeWithTransitiveDependencies_whenGetExecutionOrderCalled_thenDependenciesAreReturnedBeforeDependentInDependencyOrder()
            throws CircularReferenceException
        {
            CollectorRegistry registry = new SimpleCollectorRegistry();
            registry.register(mockCollector("vpc", Set.of()));
            registry.register(mockCollector("subnet", Set.of("vpc")));
            registry.register(mockCollector("instance", Set.of("subnet")));

            List<String> result = registry.getExecutionOrder("instance");

            assertThat(result).containsExactly("vpc", "subnet", "instance");
        }

        @Test
        void givenUnknownEntityType_whenGetExecutionOrderCalled_thenEntityTypeIsReturnedAsOnlyExecutionStep()
            throws CircularReferenceException
        {
            CollectorRegistry registry = new SimpleCollectorRegistry();

            List<String> result = registry.getExecutionOrder("unknown");

            assertThat(result).containsExactly("unknown");
        }

        @Test
        void givenEntityTypeWithDependencyThatIsNotRegistered_whenGetExecutionOrderCalled_thenMissingDependencyIsIgnored()
            throws CircularReferenceException
        {
            CollectorRegistry registry = new SimpleCollectorRegistry();
            registry.register(mockCollector("instance", Set.of("subnet")));

            List<String> result = registry.getExecutionOrder("instance");

            assertThat(result).containsExactly("instance");
        }

        @Test
        void givenNullEntityType_whenGetExecutionOrderCalled_thenExecutionOrderAcrossAllRegisteredEntityTypesIsReturned()
            throws CircularReferenceException
        {
            CollectorRegistry registry = new SimpleCollectorRegistry();
            registry.register(mockCollector("vpc", Set.of()));
            registry.register(mockCollector("subnet", Set.of("vpc")));
            registry.register(mockCollector("instance", Set.of("subnet")));

            List<String> result = registry.getExecutionOrder(null);

            assertThat(result).containsExactly("vpc", "subnet", "instance");
        }

        @Test
        void givenBlankEntityType_whenGetExecutionOrderCalled_thenExecutionOrderAcrossAllRegisteredEntityTypesIsReturned()
            throws CircularReferenceException
        {
            CollectorRegistry registry = new SimpleCollectorRegistry();
            registry.register(mockCollector("vpc", Set.of()));
            registry.register(mockCollector("subnet", Set.of("vpc")));
            registry.register(mockCollector("instance", Set.of("subnet")));

            List<String> result = registry.getExecutionOrder("   ");

            assertThat(result).containsExactly("vpc", "subnet", "instance");
        }

        @Test
        void givenMultipleIndependentRootsInInsertionOrder_whenGetExecutionOrderCalledWithNullEntityType_thenIndependentRootsRemainInInsertionOrderAndDependenciesPrecedeDependents()
            throws CircularReferenceException
        {
            CollectorRegistry registry = new SimpleCollectorRegistry();
            registry.register(mockCollector("iam-role", Set.of()));
            registry.register(mockCollector("vpc", Set.of()));
            registry.register(mockCollector("subnet", Set.of("vpc")));
            registry.register(mockCollector("instance", Set.of("subnet")));

            List<String> result = registry.getExecutionOrder(null);

            assertThat(result).containsExactly("iam-role", "vpc", "subnet", "instance");
        }

        @Test
        void givenDependencyRegisteredAfterDependent_whenGetExecutionOrderCalledWithNullEntityType_thenDependencyIsPositionedBeforeDependent()
            throws CircularReferenceException
        {
            CollectorRegistry registry = new SimpleCollectorRegistry();
            registry.register(mockCollector("instance", Set.of("subnet")));
            registry.register(mockCollector("subnet", Set.of()));

            List<String> result = registry.getExecutionOrder(null);

            assertThat(result).containsExactly("subnet", "instance");
        }

        @Test
        void givenCircularDependencyGraph_whenGetExecutionOrderCalled_thenCircularReferenceExceptionIsThrown()
        {
            CollectorRegistry registry = new SimpleCollectorRegistry();
            registry.register(mockCollector("vpc", Set.of("instance")));
            registry.register(mockCollector("subnet", Set.of("vpc")));
            registry.register(mockCollector("instance", Set.of("subnet")));

            assertThatThrownBy(() -> registry.getExecutionOrder("instance"))
                .isInstanceOf(CircularReferenceException.class)
                .hasMessageContaining("Circular dependency detected involving entity type");
        }

        @Test
        void givenCircularDependencyGraphAcrossAllRegisteredEntityTypes_whenGetExecutionOrderCalledWithNullEntityType_thenCircularReferenceExceptionIsThrown()
        {
            CollectorRegistry registry = new SimpleCollectorRegistry();
            registry.register(mockCollector("vpc", Set.of("instance")));
            registry.register(mockCollector("subnet", Set.of("vpc")));
            registry.register(mockCollector("instance", Set.of("subnet")));

            assertThatThrownBy(() -> registry.getExecutionOrder(null))
                .isInstanceOf(CircularReferenceException.class)
                .hasMessageContaining("Circular dependency detected involving entity type");
        }
    }

    @Nested
    class DfsTraversalTest
    {
        @Test
        void givenEntityTypeAlreadyVisited_whenDfsTraversalCalled_thenTraversalReturnsWithoutChanges()
            throws CircularReferenceException
        {
            Set<String> visited = new LinkedHashSet<>(Set.of("instance"));
            Set<String> visiting = new LinkedHashSet<>();
            List<String> result = new ArrayList<>(List.of("instance"));
            Map<String, Set<String>> graph = new LinkedHashMap<>();

            SimpleCollectorRegistry.dfsTraversal("instance", visited, visiting, result, graph);

            assertThat(visited).containsExactly("instance");
            assertThat(visiting).isEmpty();
            assertThat(result).containsExactly("instance");
        }

        @Test
        void givenEntityTypeCurrentlyBeingVisited_whenDfsTraversalCalled_thenCircularReferenceExceptionIsThrown()
        {
            Set<String> visited = new LinkedHashSet<>();
            Set<String> visiting = new LinkedHashSet<>(Set.of("instance"));
            List<String> result = new ArrayList<>();
            Map<String, Set<String>> graph = new LinkedHashMap<>();

            assertThatThrownBy(() -> SimpleCollectorRegistry.dfsTraversal("instance", visited, visiting, result, graph))
                .isInstanceOf(CircularReferenceException.class)
                .hasMessageContaining("Circular dependency detected involving entity type 'instance'");
        }

        @Test
        void givenAcyclicDependencyGraph_whenDfsTraversalCalled_thenDependenciesAreVisitedBeforeEntityType()
            throws CircularReferenceException
        {
            Set<String> visited = new LinkedHashSet<>();
            Set<String> visiting = new LinkedHashSet<>();
            List<String> result = new ArrayList<>();
            Map<String, Set<String>> graph = new LinkedHashMap<>();
            graph.put("instance", Set.of("subnet"));
            graph.put("subnet", Set.of("vpc"));
            graph.put("vpc", Set.of());

            SimpleCollectorRegistry.dfsTraversal("instance", visited, visiting, result, graph);

            assertThat(result).containsExactly("vpc", "subnet", "instance");
            assertThat(visited).containsExactly("vpc", "subnet", "instance");
            assertThat(visiting).isEmpty();
        }

        @Test
        void givenDependencyNotPresentInGraph_whenDfsTraversalCalled_thenMissingDependencyIsIgnored()
            throws CircularReferenceException
        {
            Set<String> visited = new LinkedHashSet<>();
            Set<String> visiting = new LinkedHashSet<>();
            List<String> result = new ArrayList<>();
            Map<String, Set<String>> graph = new LinkedHashMap<>();
            graph.put("instance", Set.of("subnet"));

            SimpleCollectorRegistry.dfsTraversal("instance", visited, visiting, result, graph);

            assertThat(result).containsExactly("instance");
            assertThat(visited).containsExactly("instance");
            assertThat(visiting).isEmpty();
        }

        @Test
        void givenIndependentNodesAlreadyInVisitedSet_whenDfsTraversalCalled_thenOnlyNewReachableNodesAreAdded()
            throws CircularReferenceException
        {
            Set<String> visited = new LinkedHashSet<>(Set.of("iam-role"));
            Set<String> visiting = new LinkedHashSet<>();
            List<String> result = new ArrayList<>(List.of("iam-role"));
            Map<String, Set<String>> graph = new LinkedHashMap<>();
            graph.put("instance", Set.of("subnet"));
            graph.put("subnet", Set.of());

            SimpleCollectorRegistry.dfsTraversal("instance", visited, visiting, result, graph);

            assertThat(result).containsExactly("iam-role", "subnet", "instance");
            assertThat(visited).containsExactly("iam-role", "subnet", "instance");
            assertThat(visiting).isEmpty();
        }
    }

    @Nested
    class UtilityMethodTest
    {
        @Test
        void givenNullElements_whenPrintCalled_thenNothingIsWritten()
        {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            PrintStream originalOut = System.out;

            try
            {
                System.setOut(new PrintStream(output, true, StandardCharsets.UTF_8));
                SimpleCollectorRegistry.print(null);
            }
            finally
            {
                System.setOut(originalOut);
            }

            assertThat(output.toString(StandardCharsets.UTF_8)).isEmpty();
        }

        @Test
        void givenElements_whenPrintCalled_thenEachElementIsWrittenOnItsOwnLine()
        {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            PrintStream originalOut = System.out;

            try
            {
                System.setOut(new PrintStream(output, true, StandardCharsets.UTF_8));
                SimpleCollectorRegistry.print(List.of("vpc", "subnet"));
            }
            finally
            {
                System.setOut(originalOut);
            }

            assertThat(output.toString(StandardCharsets.UTF_8))
                .contains("vpc")
                .contains("subnet");
        }

        @Test
        void givenMainInvoked_whenRegistryIsEmpty_thenNoOutputIsWritten()
        {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            PrintStream originalOut = System.out;

            try
            {
                System.setOut(new PrintStream(output, true, StandardCharsets.UTF_8));
                SimpleCollectorRegistry.main(new String[0]);
            }
            finally
            {
                System.setOut(originalOut);
            }

            assertThat(output.toString(StandardCharsets.UTF_8)).isEmpty();
        }
    }

    private Collector mockCollector(String entityType, Set<String> requiredEntityTypes)
    {
        Collector collector = Mockito.mock(Collector.class);
        CollectorInfo collectorInfo = CollectorInfo.builder()
            .entityType(entityType)
            .providerId("aws")
            .requiredEntityTypes(requiredEntityTypes)
            .build();

        Mockito.when(collector.getCollectorInfo()).thenReturn(collectorInfo);
        return collector;
    }
}