import React from 'react';
import { View, Text, StyleSheet, TouchableOpacity } from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import { MainTab } from '../types';
import { useStressCare } from '../context/StressCareContext';
import { LightColors, DarkColors } from '../theme/colors';

interface TabItem {
  key: MainTab;
  label: string;
  iconName: keyof typeof Ionicons.glyphMap;
}

const tabs: TabItem[] = [
  { key: 'DASHBOARD', label: 'Home', iconName: 'home' },
  { key: 'MONITORING', label: 'Live Vitals', iconName: 'pulse' },
  { key: 'ANALYTICS', label: 'Trends', iconName: 'stats-chart' },
  { key: 'INTERVENTIONS', label: 'Relief', iconName: 'leaf' },
  { key: 'PROFILE', label: 'Profile', iconName: 'person' },
];

export const BottomNavBar: React.FC = () => {
  const { currentTab, setCurrentTab, isDarkMode } = useStressCare();
  const colors = isDarkMode ? DarkColors : LightColors;

  return (
    <View style={[styles.container, { backgroundColor: colors.navBackground, borderTopColor: colors.cardBorder }]}>
      {tabs.map((tab) => {
        const isActive = currentTab === tab.key;
        const color = isActive ? colors.navActive : colors.navInactive;

        return (
          <TouchableOpacity
            key={tab.key}
            style={styles.tabButton}
            onPress={() => setCurrentTab(tab.key)}
            activeOpacity={0.7}
          >
            <Ionicons name={tab.iconName} size={22} color={color} />
            <Text style={[styles.tabLabel, { color, fontWeight: isActive ? '700' : '500' }]}>
              {tab.label}
            </Text>
            {isActive && <View style={[styles.activeIndicator, { backgroundColor: colors.navActive }]} />}
          </TouchableOpacity>
        );
      })}
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flexDirection: 'row',
    height: 64,
    borderTopWidth: 1,
    alignItems: 'center',
    justifyContent: 'space-around',
    paddingHorizontal: 8,
  },
  tabButton: {
    alignItems: 'center',
    justifyContent: 'center',
    flex: 1,
    height: '100%',
    position: 'relative',
  },
  tabLabel: {
    fontSize: 11,
    marginTop: 3,
  },
  activeIndicator: {
    width: 16,
    height: 3,
    borderRadius: 2,
    position: 'absolute',
    bottom: 4,
  },
});
