import React from 'react';
import { View, StyleSheet, Dimensions } from 'react-native';
import Svg, { Path, Defs, LinearGradient, Stop } from 'react-native-svg';
import { useStressCare } from '../context/StressCareContext';
import { LightColors, DarkColors } from '../theme/colors';

interface SparklineChartProps {
  data: number[];
  color?: string;
  height?: number;
  width?: number;
  showArea?: boolean;
}

export const SparklineChart: React.FC<SparklineChartProps> = ({
  data,
  color,
  height = 70,
  width,
  showArea = true,
}) => {
  const { isDarkMode } = useStressCare();
  const colors = isDarkMode ? DarkColors : LightColors;
  const strokeColor = color || colors.primary;

  const chartWidth = width || Dimensions.get('window').width - 72;

  if (!data || data.length < 2) {
    return <View style={{ height, width: chartWidth }} />;
  }

  const min = Math.min(...data);
  const max = Math.max(...data);
  const range = max - min || 1;

  const points = data.map((val, idx) => {
    const x = (idx / (data.length - 1)) * chartWidth;
    const y = height - ((val - min) / range) * (height - 12) - 6;
    return { x, y };
  });

  const linePath = points.reduce((acc, curr, idx) => {
    return idx === 0 ? `M ${curr.x} ${curr.y}` : `${acc} L ${curr.x} ${curr.y}`;
  }, '');

  const areaPath = `${linePath} L ${chartWidth} ${height} L 0 ${height} Z`;

  return (
    <View style={[styles.container, { height, width: chartWidth }]}>
      <Svg width={chartWidth} height={height}>
        <Defs>
          <LinearGradient id="sparklineGrad" x1="0%" y1="0%" x2="0%" y2="100%">
            <Stop offset="0%" stopColor={strokeColor} stopOpacity={0.35} />
            <Stop offset="100%" stopColor={strokeColor} stopOpacity={0.0} />
          </LinearGradient>
        </Defs>

        {showArea && <Path d={areaPath} fill="url(#sparklineGrad)" />}
        <Path d={linePath} fill="none" stroke={strokeColor} strokeWidth={2.5} strokeLinecap="round" />
      </Svg>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    overflow: 'hidden',
    alignSelf: 'center',
    marginVertical: 4,
  },
});
